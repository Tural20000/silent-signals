package com.abdullayevtural.silent_signals.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdullayevtural.silent_signals.model.EmergencyContact;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
	List<EmergencyContact> findByUserId(Long userId);

}
