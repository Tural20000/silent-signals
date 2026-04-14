package com.abdullayevtural.silent_signals.service;

import java.util.List;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.abdullayevtural.silent_signals.model.EmergencyContact;
import com.abdullayevtural.silent_signals.repository.EmergencyContactRepository;

@Service
public class RedisExpirationListener implements MessageListener {

	private final EmergencyContactRepository contactRepository;
	private final EmailService emailService;

	public RedisExpirationListener(EmergencyContactRepository contactRepository, EmailService emailService) {
		this.contactRepository = contactRepository;
		this.emailService = emailService;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {

		String expiredKey = message.toString();

		if (expiredKey.startsWith("sos_session:")) {
			Long userId = Long.parseLong(expiredKey.split(":")[1]);

			System.out.println("3 deqiqe bitdi! User " + userId + " ucun komek cagirilir......");
			List<EmergencyContact> contacts = contactRepository.findByUserId(userId);

			if (contacts.isEmpty()) {
				System.out.println("XEBERDARLIQ: Istifadecinin her bir tecili kontakti tapilmadi!");
				return;

			}
			for (EmergencyContact contact : contacts) {
				String subject = "TECILI SOS SIQNALI!";
				String body = "Salam " + contact.getContactName() + ",\n\n" + "Yaxininiz (User ID: " + userId
						+ ") SOS siqnali gonderib ve 3 deqiqe erzibde tehlukesiz oldugunu tesdiqlemeyib.\n"
						+ "Zehmet olmasa onunla elaqe saxlayin!";

				emailService.sendEmail(contact.getContactEmail(), subject, body);

			}

		}
	}
}