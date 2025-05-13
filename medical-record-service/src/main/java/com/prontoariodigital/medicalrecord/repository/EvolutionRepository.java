package com.prontoariodigital.medicalrecord.repository;

import com.prontoariodigital.medicalrecord.domain.Evolution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EvolutionRepository extends JpaRepository<Evolution, UUID> {
    Page<Evolution> findByMedicalRecordId(UUID medicalRecordId, Pageable pageable);
    Page<Evolution> findByDoctorId(UUID doctorId, Pageable pageable);
} 