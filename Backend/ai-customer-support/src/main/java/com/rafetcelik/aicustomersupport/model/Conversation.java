package com.rafetcelik.aicustomersupport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Conversation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String conversationTitle;
	
	@Lob // Büyük metinler için kullanılır
	private String conversationSummary;
	
	private boolean ticketCreated;
	
	@ManyToOne
	private Customer customer;
	
	@JsonIgnore
	@OneToOne(mappedBy = "conversation")
	private Ticket ticket;
}
