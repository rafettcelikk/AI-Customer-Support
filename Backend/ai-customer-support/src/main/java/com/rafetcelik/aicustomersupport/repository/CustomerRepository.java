package com.rafetcelik.aicustomersupport.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafetcelik.aicustomersupport.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Optional<Customer> findByEmailAddress(String email);

	Customer findByEmailAddressAndPhoneNumber(String email, String phone);

}
