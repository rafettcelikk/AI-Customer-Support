package com.rafetcelik.aicustomersupport.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafetcelik.aicustomersupport.service.chat.IAISupportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/ai")
public class AISupportController {
	private final IAISupportService aiSupportService;
	
	@PostMapping("/resolution-suggestions")
	public Mono<ResponseEntity<List<String>>> getGeneratedSuggestions(@RequestBody Map<String, String> request) {
		String complaintSummary = request.get("complaintSummary");
		if (complaintSummary == null || complaintSummary.isEmpty()) {
			return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
		}
		return aiSupportService.generateResolutionSuggestions(complaintSummary)
				.map(ResponseEntity :: ok)
				.onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}
}
