package com.rafetcelik.aicustomersupport.helper;

public record CustomerInfo(String emailAddress, String phoneNumber, String orderNumber) {
	
	public CustomerInfo(String emailAddress, String phoneNumber) {
		this(emailAddress, phoneNumber, "");
	}
}
