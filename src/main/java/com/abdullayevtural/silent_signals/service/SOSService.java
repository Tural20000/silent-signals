package com.abdullayevtural.silent_signals.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.abdullayevtural.silent_signals.dto.SOSRequest;
import com.abdullayevtural.silent_signals.model.SOSAlert;
import com.abdullayevtural.silent_signals.repository.SOSRepository;

@Service
public class SOSService {
	private final SOSRepository sosRepository;
	private final RedisService redisService;

	public SOSService(SOSRepository sosRepository, RedisService redisService) {
		this.redisService = redisService;
		this.sosRepository = sosRepository;

	}

	public void processSOS(Long userId, SOSRequest request) {
		SOSAlert alert = new SOSAlert();
		alert.setUserId(userId);
		alert.setLatitude(request.getLatitude());
		alert.setLongitude(request.getLongitude());
		alert.setStatus("ACTIVE");
		alert.setCreatedAt(LocalDateTime.now());
		sosRepository.save(alert);

		String locationInfo = "Lat: " + request.getLatitude() + ", Lon: " + request.getLongitude();
		redisService.createSOSSession(userId, locationInfo);

	}

}
