package com.rafetcelik.aicustomersupport.helper;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import com.rafetcelik.aicustomersupport.dto.ChatEntry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerInfoHelper {
	private String emailAddress;
	
	private String phoneNumber;
	
	private String orderNumber;
	
	public static CustomerInfo extractCustomerInformationFromChatHistory(List<ChatEntry> history) {
		if (history == null || history.isEmpty()) {
			return new CustomerInfo(null, null, null);
		}
		
		Optional<String> emailAddress = history.stream()
				.filter(entry -> "user".equalsIgnoreCase(entry.getRole()))
				.map(ChatEntry :: getContent)
				.filter(content -> content != null && !content.isBlank())
				.map(CustomerInfoHelper :: extractEmailAddress)
				.filter(Optional :: isPresent)
				.map(Optional :: get)
				.findFirst();
		
		Optional<String> phoneNumber = history.stream()
				.filter(entry -> "user".equalsIgnoreCase(entry.getRole()))
				.map(ChatEntry :: getContent)
				.filter(content -> content != null && !content.isBlank())
				.map(CustomerInfoHelper :: extractPhoneNumber)
				.filter(Optional :: isPresent)
				.map(Optional :: get)
				.findFirst();
		
		Optional<String> orderNumber = history.stream()
				.filter(entry -> "user".equalsIgnoreCase(entry.getRole()))
				.map(ChatEntry :: getContent)
				.filter(content -> content != null && !content.isBlank())
				.map(CustomerInfoHelper :: extractOrderNumber)
				.filter(Optional :: isPresent)
				.map(Optional :: get)
				.findFirst();
		
		return new CustomerInfo(emailAddress.orElse(null), phoneNumber.orElse(null), orderNumber.orElse(null));
	}
	
	public static CustomerInfo extractCustomerInfoFromMostCurrentMessage(String entry) {
		if (entry == null || entry.isEmpty()) {
			return new CustomerInfo(entry, entry, entry);
		}
		Optional<String> emailAddress = extractEmailAddress(entry);
		Optional<String> phoneNumber = extractPhoneNumber(entry);
		return new CustomerInfo(emailAddress.orElse(null), phoneNumber.orElse(null));
	}
	
	private static Optional<String> extractEmailAddress(String emailText) {
		return Optional.ofNullable(emailText)
				.filter(s -> !s.isEmpty())
				.flatMap(t -> {
					Matcher matcher = RegexPattern.EMAIL_PATTERN.matcher(t);
					return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
				});
	}
	
	private static Optional<String> extractPhoneNumber(String phoneText) {
		return Optional.ofNullable(phoneText)
				.filter(s -> !s.isEmpty())
				.flatMap(t -> {
					Matcher matcher = RegexPattern.PHONE_PATTERN.matcher(t);
					return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
				});
	}
	
	private static Optional<String> extractOrderNumber(String orderText) {
		return Optional.ofNullable(orderText)
				.filter(s -> !s.isEmpty())
				.flatMap(t -> {
					Matcher matcher = RegexPattern.ORDER_NUMBER_PATTERN.matcher(t);
					return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
				});
	}
}
