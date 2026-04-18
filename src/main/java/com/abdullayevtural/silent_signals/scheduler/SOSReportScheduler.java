package com.abdullayevtural.silent_signals.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.abdullayevtural.silent_signals.model.SOSStatus;
import com.abdullayevtural.silent_signals.repository.SOSRepository;

@Component
public class SOSReportScheduler {
	private static final Logger log = LoggerFactory.getLogger(SOSReportScheduler.class);

	private final SOSRepository sosRepository;

	public SOSReportScheduler(SOSRepository sosRepository) {
		this.sosRepository = sosRepository;
	}

	@Scheduled(cron = "0 0 6 * * *")
	public void generateDailyReport() {
		long active = sosRepository.countByStatus(SOSStatus.ACTIVE);
		long resolved = sosRepository.countByStatus(SOSStatus.RESOLVED);
		log.info("Günlük SOS hesabatı — {} | ACTIVE: {}, RESOLVED: {}", LocalDateTime.now(), active, resolved);
	}
}
