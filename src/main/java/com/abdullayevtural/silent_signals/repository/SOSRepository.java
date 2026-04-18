package com.abdullayevtural.silent_signals.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abdullayevtural.silent_signals.model.SOSAlert;
import com.abdullayevtural.silent_signals.model.SOSStatus;

public interface SOSRepository extends JpaRepository<SOSAlert, Long> {

	@Query("SELECT s FROM SOSAlert s JOIN FETCH s.user u WHERE u.id = :userId ORDER BY s.createdAt DESC")
	List<SOSAlert> findHistoryForUser(@Param("userId") Long userId);

	Optional<SOSAlert> findByIdAndUser_Id(Long id, Long userId);

	long countByStatus(SOSStatus status);

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM SOSAlert s WHERE s.status = :status AND s.createdAt < :cutoff")
	int deleteByStatusAndCreatedAtBefore(@Param("status") SOSStatus status, @Param("cutoff") LocalDateTime cutoff);
}
