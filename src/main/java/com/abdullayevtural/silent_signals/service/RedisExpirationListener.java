package com.abdullayevtural.silent_signals.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.abdullayevtural.silent_signals.model.EmergencyContact;
import com.abdullayevtural.silent_signals.model.User;
import com.abdullayevtural.silent_signals.repository.EmergencyContactRepository;
import com.abdullayevtural.silent_signals.repository.UserRepository;

@Service
public class RedisExpirationListener implements MessageListener {

	private static final Logger log = LoggerFactory.getLogger(RedisExpirationListener.class);

	private final EmergencyContactRepository contactRepository;
	private final UserRepository userRepository;
	private final EmailService emailService;

	public RedisExpirationListener(EmergencyContactRepository contactRepository, UserRepository userRepository,
			EmailService emailService) {
		this.contactRepository = contactRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {

		String expiredKey = message.toString();

		if (expiredKey.startsWith("sos_session:")) {
			Long userId = Long.parseLong(expiredKey.split(":")[1]);

			log.info("3 dəqiqə bitdi — istifadəçi {} üçün təcili bildiriş göndərilir", userId);
			List<EmergencyContact> contacts = contactRepository.findByUser_Id(userId);

			if (contacts.isEmpty()) {
				log.warn("Bu istifadəçi üçün təcili kontakt tapılmadı: {}", userId);
				return;
			}

			String username = userRepository.findById(userId).map(User::getUsername).orElse("user-" + userId);

			for (EmergencyContact contact : contacts) {
				String subject = "TƏCİLİ SOS SIQNALI!";
				String body = "Salam " + contact.getContactName() + ",\n\n" + "Yaxınınız (" + username
						+ ") SOS siqnalı göndərib və 3 dəqiqə ərzində təhlükəsiz olduğunu təsdiqləməyib.\n"
						+ "Zəhmətə olmasa onunla əlaqə saxlayın!";

				emailService.sendEmail(contact.getContactEmail(), subject, body);
			}
		}
	}
}
