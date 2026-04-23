package com.rafetcelik.aicustomersupport.service.customer;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rafetcelik.aicustomersupport.model.Customer;
import com.rafetcelik.aicustomersupport.repository.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService{
	private final CustomerRepository customerRepository;
	
	@Override
	public Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Müşteri bulunamadı: " + id));
	}

	@Override
	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmailAddress(email)
				.orElseThrow(() -> new EntityNotFoundException("Müşteri bulunamadı: " + email));
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Customer updateCustomer(Long id, Customer updatedCustomer) {
		return customerRepository.findById(id)
				.map(customer -> {
					customer.setFullName(updatedCustomer.getFullName());
					customer.setEmailAddress(updatedCustomer.getEmailAddress());
					customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
					return customerRepository.save(customer);
				})
				.orElseThrow(() -> new EntityNotFoundException("Müşteri bulunamadı: " + id));
	}

	@Override
	public void deleteCustomer(Long id) {
		customerRepository.deleteById(id);
	}

}
