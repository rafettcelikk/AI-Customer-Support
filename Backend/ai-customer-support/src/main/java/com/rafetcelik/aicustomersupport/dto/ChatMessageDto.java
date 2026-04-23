package com.rafetcelik.aicustomersupport.dto;

import lombok.Data;

@Data
public class ChatMessageDto {
	private String sessionId;
	
	private String message;
}
