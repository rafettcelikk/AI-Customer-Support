package com.rafetcelik.aicustomersupport.service.ticket;

import java.util.List;

import com.rafetcelik.aicustomersupport.dto.TicketDto;
import com.rafetcelik.aicustomersupport.model.Conversation;
import com.rafetcelik.aicustomersupport.model.Ticket;

public interface ITicketService {
	Ticket createTicketForConversation(Conversation conversation);
	
	TicketDto getTicketById(Long ticketId);
	
	TicketDto resolveTicket(Long ticketId, String resolutionDetails);
	
	List<TicketDto> getAllTickets();
}
