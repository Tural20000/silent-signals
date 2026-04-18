package com.abdullayevtural.silent_signals.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.silent_signals.dto.SOSAlertResponse;
import com.abdullayevtural.silent_signals.dto.SOSRequest;
import com.abdullayevtural.silent_signals.dto.SosResolveRequest;
import com.abdullayevtural.silent_signals.exception.ResourceNotFoundException;
import com.abdullayevtural.silent_signals.repository.UserRepository;
import com.abdullayevtural.silent_signals.service.SOSService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sos")
@PreAuthorize("hasRole('USER')")
public class SOSController {
	private final SOSService sosService;
	private final UserRepository userRepository;

	public SOSController(SOSService sosService, UserRepository userRepository) {
		this.sosService = sosService;
		this.userRepository = userRepository;
	}

	@PostMapping("/send")
	public ResponseEntity<SOSAlertResponse> sendSOS(@Valid @RequestBody SOSRequest request,
			Authentication authentication) {
		Long userId = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"))
				.getId();

		SOSAlertResponse body = sosService.processSOS(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@GetMapping("/history")
	public ResponseEntity<List<SOSAlertResponse>> history(Authentication authentication) {
		Long userId = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"))
				.getId();
		return ResponseEntity.ok(sosService.historyForUser(userId));
	}

	@PostMapping("/resolve")
	public ResponseEntity<SOSAlertResponse> resolve(@Valid @RequestBody SosResolveRequest request,
			Authentication authentication) {
		Long userId = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"))
				.getId();
		return ResponseEntity.ok(sosService.resolve(userId, request.getAlertId()));
	}
}
