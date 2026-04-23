package com.rafetcelik.aicustomersupport.event;

import org.springframework.context.ApplicationEvent;

import com.rafetcelik.aicustomersupport.model.Ticket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketCreationEvent extends ApplicationEvent {
	private final Ticket ticket;
	
	public TicketCreationEvent(Ticket ticket) {
		super(ticket);
		this.ticket = ticket;
	}
}
