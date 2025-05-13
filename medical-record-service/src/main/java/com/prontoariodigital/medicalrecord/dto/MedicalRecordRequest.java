package com.prontoariodigital.medicalrecord.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MedicalRecordRequest {
    @NotNull(message = "ID do paciente é obrigatório")
    private UUID patientId;

    @NotNull(message = "ID do médico é obrigatório")
    private UUID doctorId;

    @NotBlank(message = "Queixa principal é obrigatória")
    private String mainComplaint;

    private String currentDiseaseHistory;
    private String pastDiseaseHistory;
    private String familyHistory;
    private String physicalExamination;
    private String diagnosis;
    private String treatment;
    private String evolution;
} 