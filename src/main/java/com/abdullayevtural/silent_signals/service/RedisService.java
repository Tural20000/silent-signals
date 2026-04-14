package com.abdullayevtural.silent_signals.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	public RedisService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;

	}

	public void createSOSSession(Long userId, String locationData) {
		String key = "sos_session:" + userId;
		redisTemplate.opsForValue().set(key, locationData, 3, TimeUnit.MINUTES);

	}

	public boolean isSessionActive(Long userID) {
		return Boolean.TRUE.equals(redisTemplate.hasKey("sos_session:" + userID));

	}

}
