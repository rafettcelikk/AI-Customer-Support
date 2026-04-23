package com.rafetcelik.aicustomersupport.service.chat;

import java.util.List;

import com.rafetcelik.aicustomersupport.dto.ChatEntry;

import reactor.core.publisher.Mono;

public interface IAISupportService {
	Mono<String> chatWithHistory(List<ChatEntry> history);
	
	Mono<String> generateCustomerConfirmationMessage();
	
	Mono<String> summarizeCustomerConversation(String customerConversationText);
	
	Mono<String> generateConversationTitle(String summarizedConversation);
	
	Mono<String> generateEmailNotificationMessage();
	
	Mono<List<String>> generateResolutionSuggestions(String complaintSummary);
}
