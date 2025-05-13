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
public class AttachmentResponse {
    private UUID id;
    private String fileName;
    private String fileType;
    private String filePath;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 