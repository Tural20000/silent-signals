package com.abdullayevtural.silent_signals.dto;

import java.time.LocalDateTime;

import com.abdullayevtural.silent_signals.model.SOSAlert;
import com.abdullayevtural.silent_signals.model.SOSStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SOSAlertResponse {
	private Long id;
	private Long userId;
	private Double latitude;
	private Double longitude;
	private SOSStatus status;
	private LocalDateTime createdAt;

	public static SOSAlertResponse from(SOSAlert alert) {
		return new SOSAlertResponse(alert.getId(), alert.getUser().getId(), alert.getLatitude(), alert.getLongitude(),
				alert.getStatus(), alert.getCreatedAt());
	}
}
