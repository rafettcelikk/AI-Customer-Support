package com.rafetcelik.aicustomersupport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafetcelik.aicustomersupport.model.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long>{

}
