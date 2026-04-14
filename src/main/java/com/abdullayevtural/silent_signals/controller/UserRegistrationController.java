package com.abdullayevtural.silent_signals.controller;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.silent_signals.dto.UserRequest;
import com.abdullayevtural.silent_signals.model.Role;
import com.abdullayevtural.silent_signals.model.User;
import com.abdullayevtural.silent_signals.repository.RoleRepository;
import com.abdullayevtural.silent_signals.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth") // ARTIQ SecurityConfig BU YOLU TANIYACAQ
public class UserRegistrationController {

	private static final Logger log = LoggerFactory.getLogger(UserRegistrationController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/reg-user")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest request) {
		log.info("📝 Yeni qeydiyyat cəhdi: {}", request.getUsername());

		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			log.warn("⚠️ İstifadəçi adı artıq tutulub: {}", request.getUsername());
			return ResponseEntity.status(409).body("İstifadəçi artıq mövcuddur");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		user.setPhoneNumber(request.getPhoneNumber());

		Set<Role> roles = new HashSet<>();

		roleRepository.findByName("ROLE_USER").ifPresentOrElse(roles::add, () -> {
			Role defaultRole = new Role();
			defaultRole.setName("ROLE_USER");
			roles.add(roleRepository.save(defaultRole));
		});

		user.setRoles(roles);
		userRepository.save(user);

		log.info("✅ İstifadəçi uğurla qeydiyyatdan keçdi: {}", request.getUsername());
		return ResponseEntity.status(201).body("Qeydiyyat uğurla tamamlandı");
	}
}