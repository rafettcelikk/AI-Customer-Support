package com.rafetcelik.aicustomersupport.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rafetcelik.aicustomersupport.model.Conversation;
import com.rafetcelik.aicustomersupport.model.Customer;
import com.rafetcelik.aicustomersupport.model.Ticket;
import com.rafetcelik.aicustomersupport.repository.ConversationRepository;
import com.rafetcelik.aicustomersupport.repository.CustomerRepository;
import com.rafetcelik.aicustomersupport.repository.TicketRepository;
import com.rafetcelik.aicustomersupport.service.ticket.TicketService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ConversationRepository conversationRepository;
    private final TicketService ticketService;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    //@PostConstruct
    public void init() {
        generateSeedData();
    }

    private void generateSeedData() {
        List<Customer> customers = createCustomers();

        for (int i = 1; i <= 10; i++) {
            Conversation conversation = new Conversation();
            String title = "Görüşme Başlığı " + i;
            String summary = "Görüşme özeti " + i + ": Müşteri, ürünle ilgili bir sorun yaşadığını belirtiyor ve çözüm talep ediyor.";
            conversation.setConversationTitle(title);
            conversation.setConversationSummary(summary);
            conversation.setTicketCreated(false);
            conversation.setCustomer(customers.get(i % customers.size()));
            Conversation savedConversation = conversationRepository.save(conversation);
            Ticket ticket = ticketService.createTicketForConversation(savedConversation);
            Ticket savedTicket = ticketRepository.save(ticket);

            savedConversation.setTicketCreated(true);
            savedConversation.setTicket(savedTicket);
            conversationRepository.save(savedConversation);
        }
    }

    private List<Customer> createCustomers() {
        List<Customer> users = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer();
            customer.setFullName("User " + i);
            customer.setEmailAddress("email" + i + "@example.com");
            customer.setPhoneNumber("123456789" + i);
            users.add(customerRepository.save(customer));
        }
        return users;
    }
}	