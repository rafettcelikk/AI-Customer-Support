package com.rafetcelik.aicustomersupport.event;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.rafetcelik.aicustomersupport.email.EmailNotificationService;
import com.rafetcelik.aicustomersupport.model.Ticket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketCreationEventListener implements ApplicationListener<TicketCreationEvent>{
	private final EmailNotificationService emailNotificationService;
	
	@Override
	public void onApplicationEvent(TicketCreationEvent event) {
		Ticket ticket = event.getTicket();
		try {
			emailNotificationService.sendTicketNotificationEmail(ticket);
		} catch (MessagingException e) {
			log.error("E-posta gönderilirken hata oluştu: {}", e.getMessage(), e);
		}
	}
	
}
