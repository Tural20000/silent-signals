package com.abdullayevtural.silent_signals.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CleanupScheduler {

	@Scheduled(cron = "0 0 0 * * *")
	public void clearOldLogs() {
		System.out.println("Sistem təmizliyi aparılır...");

	}
}