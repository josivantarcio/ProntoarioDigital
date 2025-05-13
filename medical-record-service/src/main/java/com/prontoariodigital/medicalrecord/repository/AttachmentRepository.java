package com.prontoariodigital.medicalrecord.repository;

import com.prontoariodigital.medicalrecord.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
    Page<Attachment> findByMedicalRecordId(UUID medicalRecordId, Pageable pageable);
} 