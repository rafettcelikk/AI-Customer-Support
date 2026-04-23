package com.rafetcelik.aicustomersupport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafetcelik.aicustomersupport.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
