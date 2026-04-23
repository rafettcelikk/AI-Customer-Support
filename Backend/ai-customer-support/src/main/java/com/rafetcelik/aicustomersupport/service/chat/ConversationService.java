package com.rafetcelik.aicustomersupport.service.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.rafetcelik.aicustomersupport.dto.ChatEntry;
import com.rafetcelik.aicustomersupport.dto.ChatMessageDto;
import com.rafetcelik.aicustomersupport.event.TicketCreationEvent;
import com.rafetcelik.aicustomersupport.helper.CustomerInfo;
import com.rafetcelik.aicustomersupport.helper.CustomerInfoHelper;
import com.rafetcelik.aicustomersupport.helper.RegexPattern;
import com.rafetcelik.aicustomersupport.model.Conversation;
import com.rafetcelik.aicustomersupport.model.Customer;
import com.rafetcelik.aicustomersupport.model.Ticket;
import com.rafetcelik.aicustomersupport.repository.ConversationRepository;
import com.rafetcelik.aicustomersupport.repository.CustomerRepository;
import com.rafetcelik.aicustomersupport.repository.TicketRepository;
import com.rafetcelik.aicustomersupport.service.ticket.ITicketService;
import com.rafetcelik.aicustomersupport.utils.MessageUtil;
import com.rafetcelik.aicustomersupport.websocket.WebSocketMessageSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversationService implements IConversationService {
	private final AISupportService aiSupportService;
	private final CustomerRepository customerRepository;
	private final ConversationRepository conversationRepository;
	private final ApplicationEventPublisher publisher;
	
	private final Map<String, List<ChatEntry>> activeConversations = new ConcurrentHashMap<>();
	private final Map<String, Boolean> waitingForContactCorrection = new ConcurrentHashMap<>();
	private final WebSocketMessageSender webSocketMessageSender;
	private final ITicketService ticketService; 
	private final TicketRepository ticketRepository;

	@Override
	public String handleChatMessage(ChatMessageDto chatMessage) {
		String sessionId = chatMessage.getSessionId();
		String userMessage = chatMessage.getMessage() != null ? chatMessage.getMessage().trim() : "";
		log.info("Başlayan sohbet için sessionId: {} ", sessionId);
		log.info("Kullanıcı mesajı: {}", userMessage);
		
		List<ChatEntry> history = activeConversations.computeIfAbsent(sessionId, 
				k -> Collections.synchronizedList(new ArrayList<>()));
		
		String correctedCustomerInformation = getCorrectedCustomerInformation(sessionId, userMessage, history);
		if (correctedCustomerInformation != null) {
			return correctedCustomerInformation;
		}
		
		history.add(new ChatEntry("user", userMessage));
		
		String aiResponseText;
		try {
			aiResponseText = aiSupportService.chatWithHistory(history).block();
		} catch (Exception e) {
			log.error("AI ile sohbet sırasında hata:", e);
			aiResponseText = "Üzgünüm, şu anda size yardımcı olamıyorum. Lütfen daha sonra tekrar deneyin.";
		}
		
		if (aiResponseText == null) {
			return "";
		}
		
		history.add(new ChatEntry("assistant", aiResponseText));
		
		if (aiResponseText.contains("TICKET_CREATION_READY")) {
		    
		    // 1. ÖNCE VERİTABANI KONTROLÜ (Senkron)
		    Customer customer = getCustomerInformation(history);
		    
		    if (customer == null) {
		        // Müşteri yoksa AI'ın onay mesajı üretmesini ENGELLE ve direkt uyarı dön!
		        log.warn("Müşteri veritabanında bulunamadı. Kayıt reddedildi.");
		        waitingForContactCorrection.put(sessionId, true);
		        
		        String errorMessage = MessageUtil.INVALID_CONTACT_INFORMATION_MESSAGE;
		        history.add(new ChatEntry("assistant", errorMessage));
		        return errorMessage; 
		    }

		    // 2. MÜŞTERİ VARSA SÜRECE DEVAM ET
		    try {
		        String confirmationMessage = aiSupportService.generateCustomerConfirmationMessage().block();
		        history.add(new ChatEntry("assistant", confirmationMessage));
		        
		        // Asenkron süreci (Bilet oluşturma ve E-posta) şimdi başlat
		        finalizeConversationAndNotify(sessionId, history);
		        return confirmationMessage;
		    } catch (Exception e) {
		        log.error("Onay mesajı oluşturulurken hata:", e);
		        return "Onay mesajı oluşturulurken bir hata oluştu. Lütfen daha sonra tekrar deneyin.";
		    }
		}
		return aiResponseText;
	}

	private void finalizeConversationAndNotify(String sessionId, List<ChatEntry> history) {
		CompletableFuture.runAsync(() -> {
			try {
				Ticket ticket = finalizeConversationAndCreateTicket(sessionId);
				if (ticket != null) {
					history.add(new ChatEntry("system", "E-posta gönderildi."));
					String feedbackMessage = aiSupportService.generateEmailNotificationMessage().block();
					if (feedbackMessage != null) {
						List<ChatEntry> currentHistory = activeConversations.get(sessionId);
						if (currentHistory != null) {
							currentHistory.add(new ChatEntry("assistant", feedbackMessage));
						}
						webSocketMessageSender.sendMessageToUser(sessionId, feedbackMessage);
					}
				}
			} catch (Exception e) {
				log.error("Bilet oluşturulurken asenkron hata:", e);
			}
		});
	}
	
	private Ticket finalizeConversationAndCreateTicket(String sessionId) {
		List<ChatEntry> history = activeConversations.get(sessionId);
		
		Customer customer = getCustomerInformation(history);
		log.info("Müşteri bilgileri: {}", customer);
		if (customer == null) {
			String errorMessage = MessageUtil.INVALID_CONTACT_INFORMATION_MESSAGE;
			webSocketMessageSender.sendMessageToUser(sessionId, errorMessage);
			if (history != null) {
				history.add(new ChatEntry("system", errorMessage));
			}
			waitingForContactCorrection.put(sessionId, true);
			return null;
		}
		waitingForContactCorrection.remove(sessionId);
		
		Conversation conversation = getConversation(customer);
		try {
			String historyText = history.stream()
					.map(entry -> entry.getRole().toUpperCase() + ": " + entry.getContent())
					.collect(Collectors.joining("\n"));
					
			String conversationSummary = aiSupportService.summarizeCustomerConversation(historyText).block();
			String conversationTitle = aiSupportService.generateConversationTitle(conversationSummary).block();
			
			conversation.setConversationTitle(conversationTitle != null ? conversationTitle.trim() : "Destek Talebi");
			conversation.setConversationSummary(conversationSummary);
			Conversation savedConversation = conversationRepository.save(conversation);
	        Ticket ticket = ticketService.createTicketForConversation(conversation);

	        CustomerInfo customerInfo = getCustomerInfo(history);
	        if (customerInfo.orderNumber() != null) {
	            ticket.setProductOrderNumber(customerInfo.orderNumber());
	        }
	        else {
	        	ticket.setProductOrderNumber(null);
	        }
	        Ticket savedTicket = ticketRepository.save(ticket);
			savedConversation.setTicket(savedTicket);
			savedConversation.setTicketCreated(true);
			conversationRepository.save(savedConversation);
			publisher.publishEvent(new TicketCreationEvent(savedTicket));
			activeConversations.remove(sessionId);
			return savedTicket;
		} catch (Exception e) {
			String errorMessage = "Destek talebi oluşturulurken bir hata oluştu.";
			log.error(errorMessage, e);
			webSocketMessageSender.sendMessageToUser(sessionId, errorMessage);
			return null;
		}
	}

	private static Conversation getConversation(Customer customer) {
		Conversation conversation = new Conversation();
		conversation.setCustomer(customer);
		conversation.setTicketCreated(false);
		return conversation;
	}
	
	private Customer getCustomerInformation(List<ChatEntry> history) {
        CustomerInfo customerInfo = getCustomerInfo(history);
        return customerRepository.findByEmailAddressAndPhoneNumber(
                customerInfo.emailAddress(), customerInfo.phoneNumber());
    }

    private static CustomerInfo getCustomerInfo(List<ChatEntry> history) {
        return CustomerInfoHelper.extractCustomerInformationFromChatHistory(history);
    }
    
    private String getCorrectedCustomerInformation(String sessionId, String userMessage, List<ChatEntry> history) {
        if (Boolean.TRUE.equals(waitingForContactCorrection.get(sessionId))) {
            
            CustomerInfo customerInfo = CustomerInfoHelper.extractCustomerInfoFromMostCurrentMessage(userMessage);
            log.info("Düzeltilmiş müşteri bilgileri 1: {}", customerInfo.toString());

            // ÇÖZÜM KALKANI: Eğer müşteri e-posta VEYA telefon vermediyse, çökmek yerine uyar!
            if (customerInfo.emailAddress() == null || customerInfo.phoneNumber() == null) {
                return "Lütfen güncel e-posta adresinizi ve telefon numaranızı tek bir mesajda yazın.";
            }

            replaceOldContactInformationHistory(history, customerInfo.emailAddress(), customerInfo.phoneNumber());
            
            CustomerInfo customer = CustomerInfoHelper.extractCustomerInformationFromChatHistory(history);
            log.info("Düzeltilmiş müşteri bilgileri 2: {}", customer);
            
            Optional<Customer> customerOpt = Optional.ofNullable(customerRepository.findByEmailAddressAndPhoneNumber(customer.emailAddress(), customer.phoneNumber()));
            log.info("Düzeltilmiş müşteri bilgileri 3: {}", customerOpt);
            
            if (customerOpt.isPresent()) {
                waitingForContactCorrection.remove(sessionId);
                finalizeConversationAndNotify(sessionId, history);
                return MessageUtil.CONTACT_INFORMATION_UPDATED_MESSAGE;
            } else {
                return MessageUtil.INVALID_CONTACT_INFORMATION_MESSAGE;
            }
        }
        return null;
    }
    
    private void replaceOldContactInformationHistory(List<ChatEntry> history, String email, String phone) {
		if (history == null || history.isEmpty()) return;
		correctCustomerContactInformation(history, email, RegexPattern.EMAIL_PATTERN);
		correctCustomerContactInformation(history, phone, RegexPattern.PHONE_PATTERN);
    }
    
    /*private void correctCustomerContactInformation(List<ChatEntry> history, String newContact, Pattern pattern) {
    	if (newContact != null && newContact.isEmpty()) return;
    	
    	OptionalInt indexOpt = IntStream.range(0, history.size())
				.filter(i -> "user".equalsIgnoreCase(history.get(i).getRole()) && pattern.matcher(history.get(i).getContent()).find())
				.findFirst();
    	indexOpt.ifPresentOrElse(index -> history.set(index, new ChatEntry("user", newContact)), () -> history.add(new ChatEntry("user", newContact)));
    }*/
    
    private void correctCustomerContactInformation(List<ChatEntry> history, String newContact, Pattern pattern) {
    	if (newContact != null && newContact.isEmpty()) return;
    	
    	for (int i = 0; i < history.size(); i++) {
    		ChatEntry entry = history.get(i);
    		if ("user".equalsIgnoreCase(entry.getRole())) {
    			String content = entry.getContent();
    			Matcher matcher = pattern.matcher(content);
    			if (matcher.find()) {
					String updatedContent = matcher.replaceAll(newContact);
					history.set(i, new ChatEntry("user", updatedContent));
					log.info("Değiştirilen içerik bilgilerinin geçmiş indexi: {}, yeni içerik: {}", i, updatedContent);
				}
    		}
    	}
    }
    
  }	