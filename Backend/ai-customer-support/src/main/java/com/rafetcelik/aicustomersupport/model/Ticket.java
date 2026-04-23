package com.rafetcelik.aicustomersupport.model;

import java.time.LocalDateTime;

import com.rafetcelik.aicustomersupport.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor 
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "Reference_Number")
	private String referenceNumber;
	
	@Column(name = "Product_Order_Number")
	private String productOrderNumber;
	
	@Lob
	private String resolutionDetails;
	
	@Enumerated(EnumType.STRING)
	private TicketStatus status;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime resolvedAt;
	
	@OneToOne
	@JoinColumn(name = "conversation_id")
	private Conversation conversation;
	
	
}
