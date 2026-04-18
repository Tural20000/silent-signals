package com.abdullayevtural.silent_signals.dto;

import java.time.LocalDateTime;

import com.abdullayevtural.silent_signals.model.SOSStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SosBroadcastMessage {
	public enum EventType {
		SOS_TRIGGERED, SOS_RESOLVED
	}

	private EventType type;
	private Long alertId;
	private Long userId;
	private String username;
	private Double latitude;
	private Double longitude;
	private SOSStatus status;
	private LocalDateTime occurredAt;
}
