package com.prontoariodigital.medicalrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionResponse {
    private UUID id;
    private UUID medicalRecordId;
    private UUID doctorId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 