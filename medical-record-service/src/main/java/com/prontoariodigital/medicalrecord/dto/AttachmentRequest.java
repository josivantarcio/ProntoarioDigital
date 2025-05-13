package com.prontoariodigital.medicalrecord.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentRequest {
    @NotNull(message = "Arquivo é obrigatório")
    private MultipartFile file;

    private String description;
} 