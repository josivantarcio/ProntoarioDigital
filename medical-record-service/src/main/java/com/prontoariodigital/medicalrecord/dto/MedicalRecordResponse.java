package com.prontoariodigital.medicalrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {
    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private String mainComplaint;
    private String currentDiseaseHistory;
    private String pastDiseaseHistory;
    private String familyHistory;
    private String physicalExamination;
    private String diagnosis;
    private String treatment;
    private String evolution;
    private List<AttachmentResponse> attachments;
    private List<EvolutionResponse> evolutions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 