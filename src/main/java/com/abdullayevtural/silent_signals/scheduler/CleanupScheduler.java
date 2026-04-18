package com.abdullayevtural.silent_signals.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.abdullayevtural.silent_signals.model.SOSStatus;
import com.abdullayevtural.silent_signals.repository.SOSRepository;

@Component
public class CleanupScheduler {

	private static final Logger log = LoggerFactory.getLogger(CleanupScheduler.class);

	private final SOSRepository sosRepository;

	public CleanupScheduler(SOSRepository sosRepository) {
		this.sosRepository = sosRepository;
	}

	@Scheduled(cron = "0 0 3 * * *")
	@Transactional
	public void deleteOldResolvedAlerts() {
		LocalDateTime cutoff = LocalDateTime.now().minusDays(90);
		int removed = sosRepository.deleteByStatusAndCreatedAtBefore(SOSStatus.RESOLVED, cutoff);
		log.info("Köhnə həll olunmuş SOS qeydləri silindi: {} sətir", removed);
	}
}
