package com.abdullayevtural.silent_signals.dto;

import com.abdullayevtural.silent_signals.model.EmergencyContact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactResponse {
	private Long id;
	private String contactName;
	private String contactEmail;
	private String phoneNumber;

	public static EmergencyContactResponse from(EmergencyContact entity) {
		return new EmergencyContactResponse(entity.getId(), entity.getContactName(), entity.getContactEmail(),
				entity.getPhoneNumber());
	}
}
