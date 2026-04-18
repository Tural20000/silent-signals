package com.abdullayevtural.silent_signals.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.abdullayevtural.silent_signals.filters.JwtAuthenticationFilter;

@Configuration
public class JwtFilterRegistrationConfig {

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistration(
			JwtAuthenticationFilter jwtAuthenticationFilter) {
		FilterRegistrationBean<JwtAuthenticationFilter> bean = new FilterRegistrationBean<>(jwtAuthenticationFilter);
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 50);
		bean.setEnabled(false);
		return bean;
	}
}
