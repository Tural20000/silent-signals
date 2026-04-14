package com.abdullayevtural.silent_signals.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
	@NotBlank(message = "İstifadəçi adı boş ola bilməz")
	private String username;
	@Size(min = 6, message = "Şifrə ən az 6 simvol olmalıdır")
	private String password;
	@Email(message = "Email formatı düzgün deyil")
	private String email;
	private String phoneNumber;
}