package com.rafetcelik.aicustomersupport.service.chat;

import com.rafetcelik.aicustomersupport.dto.ChatMessageDto;

public interface IConversationService {

	String handleChatMessage(ChatMessageDto message);

}
