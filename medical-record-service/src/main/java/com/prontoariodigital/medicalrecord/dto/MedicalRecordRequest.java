package com.prontoariodigital.medicalrecord.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequest {
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotBlank(message = "Main complaint is required")
    private String mainComplaint;

    private String currentDiseaseHistory;
    private String pastDiseaseHistory;
    private String familyHistory;
    private String physicalExamination;
    private String diagnosis;
    private String treatment;
    private String evolution;
} 