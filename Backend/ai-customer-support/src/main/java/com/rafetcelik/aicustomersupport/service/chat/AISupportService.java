package com.rafetcelik.aicustomersupport.service.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import com.rafetcelik.aicustomersupport.dto.ChatEntry;
import com.rafetcelik.aicustomersupport.utils.PromptTemplates;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@AllArgsConstructor
@Service
public class AISupportService implements IAISupportService{
	private final ChatClient chatClient;
	
	@Override
	public Mono<String> chatWithHistory(List<ChatEntry> history) {
		List<Message> dialogMessages = new ArrayList<>();
		
		for (ChatEntry chatEntry : history) {
			String content = chatEntry.getContent();
			String role = chatEntry.getRole();
			
			if ("user".equalsIgnoreCase(role)) {
				dialogMessages.add(new UserMessage(content));
			} else if ("assistant".equalsIgnoreCase(role)) {
				dialogMessages.add(new AssistantMessage(content));
			}
		}
		
		return Mono.fromCallable(() -> {
			ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
					.system(PromptTemplates.AI_SUPPORT_PROMPT)
					.messages(dialogMessages)
					.call();
			
			String content = responseSpec.content();
			if (content == null) {
				throw new IllegalStateException("İçerik alınamadı");
			}
			return content;
		}).subscribeOn(Schedulers.boundedElastic());
	}
	
	@Override
	public Mono<String> generateCustomerConfirmationMessage() {
		return Mono.fromCallable(() -> {
			ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
					.system(PromptTemplates.CUSTOMER_CONFIRMATION_PROMPT)
					.messages(new UserMessage("Müşteri destek talebini oluşturmayı onayladı. Lütfen Türkçe, kibar bir kapanış mesajı üret."))
					.call();
			
			String content = responseSpec.content();
			if (content == null) {
				throw new IllegalStateException("Yapay zeka tarafından oluşturulan onay mesajı alınamadı");
			}
			return content;
		}).subscribeOn(Schedulers.boundedElastic());
	}
	
	@Override
	public Mono<String> summarizeCustomerConversation(String customerConversationText) {
		return Mono.fromCallable(() -> {
			ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
					.system(PromptTemplates.CUSTOMER_CONVERSATION_SUMMARY_PROMPT)
					.messages(new UserMessage(customerConversationText))
					.call();
			
			String content = responseSpec.content();
			if (content == null) {
				throw new IllegalStateException("Yapay zeka tarafından oluşturulan özet mesajı alınamadı");
			}
			return content.trim();
		}).subscribeOn(Schedulers.boundedElastic());
	}
	
	@Override
	public Mono<String> generateConversationTitle(String summarizedConversation) {
		return Mono.fromCallable(() -> {
			ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
					.system(PromptTemplates.TITLE_GENERATION_PROMPT)
					.messages(new UserMessage(summarizedConversation))
					.call();
			
			String content = responseSpec.content();
			if (content == null) {
				throw new IllegalStateException("Yapay zeka tarafından oluşturulan başlık alınamadı");
			}
			return content.trim();
		}).subscribeOn(Schedulers.boundedElastic());
	}
	
	@Override
	public Mono<String> generateEmailNotificationMessage() {
		return Mono.fromCallable(() -> {
			ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
					.system(PromptTemplates.EMAIL_NOTIFICATION_PROMPT)
					.messages(new UserMessage("Müşteri için destek talebi başarıyla oluşturuldu. Lütfen bilgilendirme e-postası metnini üret."))
					.call();
			
			String content = responseSpec.content();
			if (content == null) {
				throw new IllegalStateException("Yapay zeka tarafından oluşturulan e-posta bildirim mesajı alınamadı");
			}
			return content.trim();
		}).subscribeOn(Schedulers.boundedElastic());
	}
	
	@Override
	public Mono<List<String>> generateResolutionSuggestions(String complaintSummary) {
	    String safeSummary = complaintSummary != null ? complaintSummary.replace("%", "%%") : "Belirtilmemiş";
	    
	    String prompt = String.format(PromptTemplates.RESOLUTION_SUGGESTIONS_PROMPT, safeSummary);
	    
	    return Mono.fromCallable(() -> {
	        String content = chatClient.prompt()
	                .user(prompt) 
	                .call()
	                .content();
	        
	        if (content == null || content.isBlank()) {
	            throw new IllegalStateException("Yapay zeka tarafından oluşturulan çözüm önerileri alınamadı");
	        }
	        
	        return parseResolutionSuggestions(content);
	        
	    }).subscribeOn(Schedulers.boundedElastic());
	}

	private List<String> parseResolutionSuggestions(String aiResponse) {
	    return Arrays.stream(aiResponse.split("\n"))
	            .filter(line -> line.matches("^\\d\\.\\s.*"))
	            .map(line -> line.replaceFirst("^\\d\\.\\s", "").trim())
	            .toList();
	}
}