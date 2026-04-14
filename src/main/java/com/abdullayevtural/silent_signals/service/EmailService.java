package com.abdullayevtural.silent_signals.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendEmail(String to, String subject, String body) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("silent-signals@gmail.com");
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);

			mailSender.send(message);
			System.out.println("Email ugurla gonderildi:" + to);

		} catch (Exception e) {
			System.out.println("Email gonderilerken xeta bas verdi: " + e.getMessage());
		}
	}

}
