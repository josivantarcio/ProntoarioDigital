package com.prontoariodigital.medicalrecord.service.impl;

import com.prontoariodigital.medicalrecord.config.FileStorageConfig;
import com.prontoariodigital.medicalrecord.domain.Attachment;
import com.prontoariodigital.medicalrecord.domain.Evolution;
import com.prontoariodigital.medicalrecord.domain.MedicalRecord;
import com.prontoariodigital.medicalrecord.dto.*;
import com.prontoariodigital.medicalrecord.exception.ResourceNotFoundException;
import com.prontoariodigital.medicalrecord.repository.AttachmentRepository;
import com.prontoariodigital.medicalrecord.repository.EvolutionRepository;
import com.prontoariodigital.medicalrecord.repository.MedicalRecordRepository;
import com.prontoariodigital.medicalrecord.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AttachmentRepository attachmentRepository;
    private final EvolutionRepository evolutionRepository;
    private final FileStorageConfig fileStorageConfig;

    @Override
    @Transactional
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .mainComplaint(request.getMainComplaint())
                .currentDiseaseHistory(request.getCurrentDiseaseHistory())
                .pastDiseaseHistory(request.getPastDiseaseHistory())
                .familyHistory(request.getFamilyHistory())
                .physicalExamination(request.getPhysicalExamination())
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .evolution(request.getEvolution())
                .build();

        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return mapToResponse(medicalRecord);
    }

    @Override
    @Transactional
    public MedicalRecordResponse update(UUID id, MedicalRecordRequest request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        medicalRecord.setPatientId(request.getPatientId());
        medicalRecord.setDoctorId(request.getDoctorId());
        medicalRecord.setMainComplaint(request.getMainComplaint());
        medicalRecord.setCurrentDiseaseHistory(request.getCurrentDiseaseHistory());
        medicalRecord.setPastDiseaseHistory(request.getPastDiseaseHistory());
        medicalRecord.setFamilyHistory(request.getFamilyHistory());
        medicalRecord.setPhysicalExamination(request.getPhysicalExamination());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setEvolution(request.getEvolution());

        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return mapToResponse(medicalRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponse findById(UUID id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        return mapToResponse(medicalRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalRecordResponse> findByPatientId(UUID patientId, Pageable pageable) {
        return medicalRecordRepository.findByPatientId(patientId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalRecordResponse> findByDoctorId(UUID doctorId, Pageable pageable) {
        return medicalRecordRepository.findByDoctorId(doctorId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        medicalRecordRepository.delete(medicalRecord);
    }

    @Override
    @Transactional
    public AttachmentResponse addAttachment(UUID medicalRecordId, AttachmentRequest request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        try {
            String fileName = UUID.randomUUID() + "_" + request.getFile().getOriginalFilename();
            Path uploadPath = Paths.get(fileStorageConfig.getUploadDir());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(request.getFile().getInputStream(), filePath);

            Attachment attachment = Attachment.builder()
                    .medicalRecord(medicalRecord)
                    .fileName(fileName)
                    .fileType(request.getFile().getContentType())
                    .filePath(filePath.toString())
                    .description(request.getDescription())
                    .build();

            attachment = attachmentRepository.save(attachment);
            return mapToAttachmentResponse(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    @Transactional
    public void removeAttachment(UUID medicalRecordId, UUID attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));

        if (!attachment.getMedicalRecord().getId().equals(medicalRecordId)) {
            throw new ResourceNotFoundException("Attachment not found in medical record");
        }

        try {
            Files.deleteIfExists(Paths.get(attachment.getFilePath()));
            attachmentRepository.delete(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttachmentResponse> getAttachments(UUID medicalRecordId, Pageable pageable) {
        return attachmentRepository.findByMedicalRecordId(medicalRecordId, pageable)
                .map(this::mapToAttachmentResponse);
    }

    @Override
    @Transactional
    public EvolutionResponse addEvolution(UUID medicalRecordId, EvolutionRequest request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        Evolution evolution = Evolution.builder()
                .medicalRecord(medicalRecord)
                .doctorId(request.getDoctorId())
                .description(request.getDescription())
                .evolutionDate(LocalDateTime.now())
                .build();

        evolution = evolutionRepository.save(evolution);
        return mapToEvolutionResponse(evolution);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EvolutionResponse> getEvolutions(UUID medicalRecordId, Pageable pageable) {
        return evolutionRepository.findByMedicalRecordId(medicalRecordId, pageable)
                .map(this::mapToEvolutionResponse);
    }

    private MedicalRecordResponse mapToResponse(MedicalRecord medicalRecord) {
        return MedicalRecordResponse.builder()
                .id(medicalRecord.getId())
                .patientId(medicalRecord.getPatientId())
                .doctorId(medicalRecord.getDoctorId())
                .mainComplaint(medicalRecord.getMainComplaint())
                .currentDiseaseHistory(medicalRecord.getCurrentDiseaseHistory())
                .pastDiseaseHistory(medicalRecord.getPastDiseaseHistory())
                .familyHistory(medicalRecord.getFamilyHistory())
                .physicalExamination(medicalRecord.getPhysicalExamination())
                .diagnosis(medicalRecord.getDiagnosis())
                .treatment(medicalRecord.getTreatment())
                .evolution(medicalRecord.getEvolution())
                .attachments(medicalRecord.getAttachments().stream()
                        .map(this::mapToAttachmentResponse)
                        .toList())
                .evolutions(medicalRecord.getEvolutions().stream()
                        .map(this::mapToEvolutionResponse)
                        .toList())
                .createdAt(medicalRecord.getCreatedAt())
                .updatedAt(medicalRecord.getUpdatedAt())
                .build();
    }

    private AttachmentResponse mapToAttachmentResponse(Attachment attachment) {
        return AttachmentResponse.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .filePath(attachment.getFilePath())
                .description(attachment.getDescription())
                .createdAt(attachment.getCreatedAt())
                .updatedAt(attachment.getUpdatedAt())
                .build();
    }

    private EvolutionResponse mapToEvolutionResponse(Evolution evolution) {
        return EvolutionResponse.builder()
                .id(evolution.getId())
                .medicalRecordId(evolution.getMedicalRecord().getId())
                .doctorId(evolution.getDoctorId())
                .description(evolution.getDescription())
                .createdAt(evolution.getCreatedAt())
                .updatedAt(evolution.getUpdatedAt())
                .build();
    }
} 