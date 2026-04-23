package com.rafetcelik.aicustomersupport.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafetcelik.aicustomersupport.dto.TicketDto;
import com.rafetcelik.aicustomersupport.service.ticket.ITicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/tickets")
public class TicketController {
	private final ITicketService ticketService;
	
	@GetMapping("/{ticketId}")
	public ResponseEntity<TicketDto> getTicketById(@PathVariable Long ticketId) {
		return ResponseEntity.ok(ticketService.getTicketById(ticketId));
	}
	
	@GetMapping
	public ResponseEntity<List<TicketDto>> getAllTickets() {
		return ResponseEntity.ok(ticketService.getAllTickets());
	}
	
	@PutMapping("/{id}/resolve")
	public ResponseEntity<TicketDto> resolveTicket(@PathVariable Long id, 
			@RequestBody Map<String, String> request) {
		String resolutionDetails = request.get("resolutionDetails");
		TicketDto resolvedTicket = ticketService.resolveTicket(id, resolutionDetails);
		return ResponseEntity.ok(resolvedTicket);
	}
}
