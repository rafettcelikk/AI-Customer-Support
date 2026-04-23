package com.rafetcelik.aicustomersupport.service.ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafetcelik.aicustomersupport.dto.TicketDto;
import com.rafetcelik.aicustomersupport.enums.TicketStatus;
import com.rafetcelik.aicustomersupport.model.Conversation;
import com.rafetcelik.aicustomersupport.model.Ticket;
import com.rafetcelik.aicustomersupport.repository.TicketRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService{
	
	private final TicketRepository ticketRepository;
	
	private final ObjectMapper objectMapper;

	@Override
	public Ticket createTicketForConversation(Conversation conversation) {
		Ticket ticket = new Ticket();
		ticket.setConversation(conversation);
		ticket.setStatus(TicketStatus.OPENED);
		ticket.setResolvedAt(null);
		ticket.setCreatedAt(LocalDateTime.now());
		ticket.setReferenceNumber(generateRandomAlphanumeric());
		return ticket;
	}

	@Override
	public TicketDto getTicketById(Long ticketId) {
		return ticketRepository.findById(ticketId)
				.map(ticket -> objectMapper.convertValue(ticket, TicketDto.class))
				.orElseThrow(() -> new EntityNotFoundException(ticketId + " id'li bilet bulunamadı"));
	}

	@Override
	public TicketDto resolveTicket(Long ticketId, String resolutionDetails) {
		if (resolutionDetails == null || resolutionDetails.isEmpty()) {
			throw new IllegalArgumentException("Çözüm detayları boş olamaz");
		}
		return ticketRepository.findById(ticketId).map(ticket -> {
			ticket.setResolutionDetails(resolutionDetails);
			ticket.setStatus(TicketStatus.RESOLVED);
			ticket.setResolvedAt(LocalDateTime.now());
			Ticket updatedTicket = ticketRepository.save(ticket);
			return objectMapper.convertValue(updatedTicket, TicketDto.class);
		}).orElseThrow(() -> new EntityNotFoundException(ticketId + " id'li destek talebi bulunamadı"));
	}

	@Override
	public List<TicketDto> getAllTickets() {
		return ticketRepository.findAll()
				.stream()
				.map(ticket -> objectMapper.convertValue(ticket, TicketDto.class))
				.collect(Collectors.toList());
	}
	
	private String generateRandomAlphanumeric() {
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
				.withinRange('0', 'z')
				.filteredBy(Character::isLetterOrDigit)
				.get();
		return generator.generate(10).toUpperCase();
	}

}
