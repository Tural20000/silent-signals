package com.abdullayevtural.silent_signals.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.silent_signals.model.EmergencyContact;
import com.abdullayevtural.silent_signals.repository.EmergencyContactRepository;

@RestController
@RequestMapping("/api/contacts")
public class EmergencyContactController {

	private final EmergencyContactRepository contactRepository;

	public EmergencyContactController(EmergencyContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	@PostMapping("/add")
	public String addContact(@RequestBody EmergencyContact contact) {
		contactRepository.save(contact);
		return "Kontakt uğurla əlavə edildi!";
	}

	@GetMapping("/user/{userId}")
	public List<EmergencyContact> getContacts(@PathVariable Long userId) {
		return contactRepository.findByUserId(userId);
	}
}