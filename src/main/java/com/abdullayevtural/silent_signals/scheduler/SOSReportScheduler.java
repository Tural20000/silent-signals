package com.abdullayevtural.silent_signals.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SOSReportScheduler {

	@Scheduled(cron = "0 0 0 * * *")
	public void generateDailyReport() {

		System.out.println("📊 Gündəlik Hesabat Hazırlanır: " + LocalDateTime.now());

	}
}