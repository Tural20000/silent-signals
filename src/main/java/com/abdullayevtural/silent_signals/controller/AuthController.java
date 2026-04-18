package com.abdullayevtural.silent_signals.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.silent_signals.dto.AuthRequest;
import com.abdullayevtural.silent_signals.dto.AuthResponse;
import com.abdullayevtural.silent_signals.dto.TokenRequest;
import com.abdullayevtural.silent_signals.service.UserDetailsServiceImpl;
import com.abdullayevtural.silent_signals.utils.JwtUtil;
import com.abdullayevtural.silent_signals.utils.RefreshTokenUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private RefreshTokenUtil refreshTokenUtil;

	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthRequest authRequest) {
		log.info("Login cəhdi: {}", authRequest.getUsername());

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
			log.warn("Uğursuz login cəhdi: {}", authRequest.getUsername());
			return ResponseEntity.status(401).body(Map.of("message", "İstifadəçi adı və ya şifrə səhvdir"));
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		final String refreshToken = refreshTokenUtil.generateRefreshToken(userDetails);

		log.info("Login uğurlu: {}", authRequest.getUsername());
		return ResponseEntity.ok(new AuthResponse(jwt, refreshToken));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRequest tokenRequest) {
		log.info("Refresh token istifadə olunur");

		String refreshToken = tokenRequest.getRefreshToken();
		String username = refreshTokenUtil.extractUsername(refreshToken);

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		if (refreshTokenUtil.validateToken(refreshToken, userDetails)) {
			final String newAccessToken = jwtUtil.generateToken(userDetails);
			log.info("Yeni access token yaradıldı: {}", username);
			return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
		}
		log.error("Keçərsiz refresh token: {}", username);
		return ResponseEntity.status(403).body(Map.of("message", "Keçərsiz refresh token"));
	}
}
