package com.abdullayevtural.silent_signals.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abdullayevtural.silent_signals.dto.SOSAlertResponse;
import com.abdullayevtural.silent_signals.dto.SOSRequest;
import com.abdullayevtural.silent_signals.dto.SosBroadcastMessage;
import com.abdullayevtural.silent_signals.exception.ConflictException;
import com.abdullayevtural.silent_signals.exception.ResourceNotFoundException;
import com.abdullayevtural.silent_signals.model.EmergencyContact;
import com.abdullayevtural.silent_signals.model.SOSAlert;
import com.abdullayevtural.silent_signals.model.SOSStatus;
import com.abdullayevtural.silent_signals.model.User;
import com.abdullayevtural.silent_signals.repository.EmergencyContactRepository;
import com.abdullayevtural.silent_signals.repository.SOSRepository;
import com.abdullayevtural.silent_signals.repository.UserRepository;

@Service
public class SOSService {
	private final SOSRepository sosRepository;
	private final UserRepository userRepository;
	private final EmergencyContactRepository emergencyContactRepository;
	private final ObjectProvider<RedisService> redisServiceProvider;
	private final EmailService emailService;
	private final SimpMessagingTemplate messagingTemplate;

	public SOSService(SOSRepository sosRepository, UserRepository userRepository,
			EmergencyContactRepository emergencyContactRepository, ObjectProvider<RedisService> redisServiceProvider,
			EmailService emailService, SimpMessagingTemplate messagingTemplate) {
		this.sosRepository = sosRepository;
		this.userRepository = userRepository;
		this.emergencyContactRepository = emergencyContactRepository;
		this.redisServiceProvider = redisServiceProvider;
		this.emailService = emailService;
		this.messagingTemplate = messagingTemplate;
	}

	@Transactional
	public SOSAlertResponse processSOS(Long userId, SOSRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"));
		SOSAlert alert = new SOSAlert();
		alert.setUser(user);
		alert.setLatitude(request.getLatitude());
		alert.setLongitude(request.getLongitude());
		alert.setStatus(SOSStatus.ACTIVE);
		alert.setCreatedAt(LocalDateTime.now());
		alert = sosRepository.save(alert);

		RedisService redisService = redisServiceProvider.getIfAvailable();
		if (redisService != null) {
			String locationInfo = "Lat: " + request.getLatitude() + ", Lon: " + request.getLongitude();
			redisService.createSOSSession(userId, locationInfo);
		}

		notifyContactsImmediate(user, alert, request);
		broadcast(alert, user, SosBroadcastMessage.EventType.SOS_TRIGGERED);

		return SOSAlertResponse.from(alert);
	}

	private void notifyContactsImmediate(User user, SOSAlert alert, SOSRequest request) {
		List<EmergencyContact> contacts = emergencyContactRepository.findByUser_Id(user.getId());
		if (contacts.isEmpty()) {
			return;
		}
		String subject = "SOS siqnalı: " + user.getUsername();
		String body = String.format("Təcili: %s SOS siqnalı göndərib.%nKoordinatlar: Lat %s, Lon %s%nVaxt: %s",
				user.getUsername(), request.getLatitude(), request.getLongitude(), alert.getCreatedAt());
		for (EmergencyContact c : contacts) {
			emailService.sendEmail(c.getContactEmail(), subject, body);
		}
	}

	private void broadcast(SOSAlert alert, User user, SosBroadcastMessage.EventType type) {
		SosBroadcastMessage msg = new SosBroadcastMessage(type, alert.getId(), user.getId(), user.getUsername(),
				alert.getLatitude(), alert.getLongitude(), alert.getStatus(),
				type == SosBroadcastMessage.EventType.SOS_RESOLVED ? LocalDateTime.now() : alert.getCreatedAt());
		messagingTemplate.convertAndSend("/topic/sos", msg);
	}

	@Transactional(readOnly = true)
	public List<SOSAlertResponse> historyForUser(Long userId) {
		return sosRepository.findHistoryForUser(userId).stream().map(SOSAlertResponse::from).toList();
	}

	@Transactional
	public SOSAlertResponse resolve(Long userId, Long alertId) {
		SOSAlert alert = sosRepository.findByIdAndUser_Id(alertId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("SOS tapılmadı"));
		if (alert.getStatus() != SOSStatus.ACTIVE) {
			throw new ConflictException("SOS artıq həll olunub və ya ləğv edilib");
		}
		alert.setStatus(SOSStatus.RESOLVED);
		alert = sosRepository.save(alert);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("İstifadəçi tapılmadı"));
		broadcast(alert, user, SosBroadcastMessage.EventType.SOS_RESOLVED);
		return SOSAlertResponse.from(alert);
	}
}
