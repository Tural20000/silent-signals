package com.abdullayevtural.silent_signals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
	private String jwt;
	private String refreshToken;
}