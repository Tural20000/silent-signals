package com.abdullayevtural.silent_signals.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SosResolveRequest {
	@NotNull(message = "alertId is required")
	private Long alertId;
}
