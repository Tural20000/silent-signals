package com.abdullayevtural.silent_signals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.silent_signals.dto.SOSRequest;
import com.abdullayevtural.silent_signals.model.User;
import com.abdullayevtural.silent_signals.repository.UserRepository;
import com.abdullayevtural.silent_signals.service.SOSService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sos")
public class SOSController {
	private final SOSService sosService;
	private final UserRepository userRepository;

	public SOSController(SOSService sosService, UserRepository userRepository) {
		this.sosService = sosService;
		this.userRepository = userRepository;

	}

	@PostMapping("/send")
	public ResponseEntity<String> sendSOS(@Valid @RequestBody SOSRequest request, Authentication authentication) {
		String username = authentication.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Istifadeci tapilmadi"));

		sosService.processSOS(user.getId(), request);

		return ResponseEntity.ok("SOS siqnali ugurla gonderildi ve 3 deqiqelik taymer basladi,");

	}

}
