package com.rafetcelik.aicustomersupport.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafetcelik.aicustomersupport.dto.ChatMessageDto;
import com.rafetcelik.aicustomersupport.service.chat.IConversationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${api.prefix}/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	private final IConversationService conversationService;
	
	@PostMapping
	public ResponseEntity<String> handleChatMessage(@RequestBody ChatMessageDto message) {
		String response = conversationService.handleChatMessage(message);
		log.info("Yapay Zekanın yanıtı: {}", response);
		return ResponseEntity.ok(response);
	}
}
