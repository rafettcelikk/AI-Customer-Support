package com.rafetcelik.aicustomersupport.dto;

import java.time.LocalDateTime;

import com.rafetcelik.aicustomersupport.enums.TicketStatus;
import com.rafetcelik.aicustomersupport.model.Conversation;

import lombok.Data;

@Data
public class TicketDto {
	private Long id;
	
	private String referenceNumber;
	
	private String productOrderNumber;
	
	private String resolutionDetails;
	
	private TicketStatus status;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime resolvedAt;
	
	private Conversation conversation;
}
