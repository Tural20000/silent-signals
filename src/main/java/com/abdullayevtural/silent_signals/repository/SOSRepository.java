package com.abdullayevtural.silent_signals.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdullayevtural.silent_signals.model.SOSAlert;

public interface SOSRepository extends JpaRepository<SOSAlert, Long> {
	List<SOSAlert> findByUserId(Long userId);

}
