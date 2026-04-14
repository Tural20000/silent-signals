package com.abdullayevtural.silent_signals.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.silent_signals.dto.EmergencyContactRequest;
import com.abdullayevtural.silent_signals.model.EmergencyContact;
import com.abdullayevtural.silent_signals.repository.EmergencyContactRepository;
import com.abdullayevtural.silent_signals.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contacts")
public class EmergencyContactController {

	private final EmergencyContactRepository contactRepository;
	private final UserRepository userRepository;

	public EmergencyContactController(EmergencyContactRepository contactRepository, UserRepository userRepository) {
		this.contactRepository = contactRepository;
		this.userRepository = userRepository;
	}

	@PostMapping("/add")
	public String addContact(@Valid @RequestBody EmergencyContactRequest request, Authentication authentication) {
		Long userId = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"))
				.getId();

		EmergencyContact contact = new EmergencyContact();
		contact.setUserId(userId);
		contact.setContactName(request.getContactName());
		contact.setContactEmail(request.getContactEmail());
		contact.setPhoneNumber(request.getPhoneNumber());

		contactRepository.save(contact);
		return "Kontakt uğurla əlavə edildi!";
	}

	@GetMapping("/me")
	public List<EmergencyContact> getContacts(Authentication authentication) {
		Long userId = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"))
				.getId();
		return contactRepository.findByUserId(userId);
	}
}