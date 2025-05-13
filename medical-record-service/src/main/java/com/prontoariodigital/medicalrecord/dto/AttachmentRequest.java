package com.prontoariodigital.medicalrecord.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentRequest {
    @NotNull(message = "Arquivo é obrigatório")
    private MultipartFile file;

    private String description;
} 