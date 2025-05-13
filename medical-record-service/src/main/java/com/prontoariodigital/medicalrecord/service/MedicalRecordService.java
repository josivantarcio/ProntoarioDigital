package com.prontoariodigital.medicalrecord.service;

import com.prontoariodigital.medicalrecord.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MedicalRecordService {
    MedicalRecordResponse create(MedicalRecordRequest request);
    MedicalRecordResponse update(UUID id, MedicalRecordRequest request);
    MedicalRecordResponse findById(UUID id);
    Page<MedicalRecordResponse> findByPatientId(UUID patientId, Pageable pageable);
    Page<MedicalRecordResponse> findByDoctorId(UUID doctorId, Pageable pageable);
    void delete(UUID id);
    
    // Attachment methods
    AttachmentResponse addAttachment(UUID medicalRecordId, AttachmentRequest request);
    void removeAttachment(UUID medicalRecordId, UUID attachmentId);
    Page<AttachmentResponse> getAttachments(UUID medicalRecordId, Pageable pageable);
    
    // Evolution methods
    EvolutionResponse addEvolution(UUID medicalRecordId, EvolutionRequest request);
    Page<EvolutionResponse> getEvolutions(UUID medicalRecordId, Pageable pageable);
} 