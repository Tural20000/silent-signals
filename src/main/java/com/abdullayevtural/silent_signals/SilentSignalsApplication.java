package com.abdullayevtural.silent_signals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SilentSignalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SilentSignalsApplication.class, args);
	}
}
