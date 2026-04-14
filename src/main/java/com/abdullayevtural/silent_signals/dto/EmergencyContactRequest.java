package com.abdullayevtural.silent_signals.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmergencyContactRequest {

	@NotBlank(message = "Contact name is required")
	private String contactName;

	@Email(message = "Contact email must be valid")
	@NotBlank(message = "Contact email is required")
	private String contactEmail;

	@NotBlank(message = "Phone number is required")
	private String phoneNumber;
}
