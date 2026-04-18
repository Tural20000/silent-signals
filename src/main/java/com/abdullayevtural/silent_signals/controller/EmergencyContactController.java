package com.abdullayevtural.silent_signals.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.silent_signals.dto.EmergencyContactRequest;
import com.abdullayevtural.silent_signals.dto.EmergencyContactResponse;
import com.abdullayevtural.silent_signals.exception.ResourceNotFoundException;
import com.abdullayevtural.silent_signals.model.EmergencyContact;
import com.abdullayevtural.silent_signals.model.User;
import com.abdullayevtural.silent_signals.repository.EmergencyContactRepository;
import com.abdullayevtural.silent_signals.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contacts")
@PreAuthorize("hasRole('USER')")
public class EmergencyContactController {

	private final EmergencyContactRepository contactRepository;
	private final UserRepository userRepository;

	public EmergencyContactController(EmergencyContactRepository contactRepository, UserRepository userRepository) {
		this.contactRepository = contactRepository;
		this.userRepository = userRepository;
	}

	@PostMapping
	public ResponseEntity<EmergencyContactResponse> addContact(@Valid @RequestBody EmergencyContactRequest request,
			Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"));

		EmergencyContact contact = new EmergencyContact();
		contact.setUser(user);
		contact.setContactName(request.getContactName());
		contact.setContactEmail(request.getContactEmail());
		contact.setPhoneNumber(request.getPhoneNumber());

		EmergencyContact saved = contactRepository.save(contact);
		return ResponseEntity.status(HttpStatus.CREATED).body(EmergencyContactResponse.from(saved));
	}

	@GetMapping("/me")
	public ResponseEntity<List<EmergencyContactResponse>> getContacts(Authentication authentication) {
		Long userId = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"))
				.getId();
		List<EmergencyContactResponse> list = contactRepository.findByUser_Id(userId).stream()
				.map(EmergencyContactResponse::from).toList();
		return ResponseEntity.ok(list);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable Long id, Authentication authentication) {
		Long userId = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"))
				.getId();
		var contact = contactRepository.findByIdAndUser_Id(id, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Kontakt tapılmadı"));
		contactRepository.delete(contact);
		return ResponseEntity.noContent().build();
	}
}
