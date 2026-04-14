package com.abdullayevtural.silent_signals.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SOSReportScheduler {
	private static final Logger log = LoggerFactory.getLogger(SOSReportScheduler.class);

	@Scheduled(cron = "0 0 0 * * *")
	public void generateDailyReport() {

		log.info("Gundelik hesabat hazirlanir: {}", LocalDateTime.now());

	}
}