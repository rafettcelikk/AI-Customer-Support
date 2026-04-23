package com.rafetcelik.aicustomersupport.service.customer;

import java.util.List;

import com.rafetcelik.aicustomersupport.model.Customer;

public interface ICustomerService {
	Customer createCustomer(Customer customer);	
	
	Customer getCustomerById(Long id);
	
	Customer getCustomerByEmail(String email);
	
	List<Customer> getAllCustomers();
	
	Customer updateCustomer(Long id, Customer updatedCustomer);
	
	void deleteCustomer(Long id);
}
