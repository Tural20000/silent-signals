package com.abdullayevtural.silent_signals.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdullayevtural.silent_signals.model.EmergencyContact;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
	List<EmergencyContact> findByUser_Id(Long userId);

	Optional<EmergencyContact> findByIdAndUser_Id(Long id, Long userId);
}
