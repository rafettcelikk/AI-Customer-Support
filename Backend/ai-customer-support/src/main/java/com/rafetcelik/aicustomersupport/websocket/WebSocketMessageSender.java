package com.rafetcelik.aicustomersupport.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketMessageSender {
	private final SimpMessagingTemplate messagingTemplate;
	
	public void sendMessageToUser(String sessionId, String message) {
		String destination = "/topic/message/" + sessionId;
		messagingTemplate.convertAndSend(destination, message);
	}
}
