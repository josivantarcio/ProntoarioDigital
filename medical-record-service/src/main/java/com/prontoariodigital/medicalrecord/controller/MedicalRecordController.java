package com.prontoariodigital.medicalrecord.controller;

import com.prontoariodigital.medicalrecord.dto.*;
import com.prontoariodigital.medicalrecord.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Endpoints for managing medical records, attachments, and evolutions")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @Operation(summary = "Create a new medical record")
    public ResponseEntity<MedicalRecordResponse> create(@Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecordService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing medical record")
    public ResponseEntity<MedicalRecordResponse> update(@PathVariable UUID id, @Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(medicalRecordService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a medical record by its ID")
    public ResponseEntity<MedicalRecordResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(medicalRecordService.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all medical records for a patient")
    public ResponseEntity<Page<MedicalRecordResponse>> getByPatientId(@PathVariable UUID patientId, Pageable pageable) {
        return ResponseEntity.ok(medicalRecordService.findByPatientId(patientId, pageable));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get all medical records for a doctor")
    public ResponseEntity<Page<MedicalRecordResponse>> getByDoctorId(@PathVariable UUID doctorId, Pageable pageable) {
        return ResponseEntity.ok(medicalRecordService.findByDoctorId(doctorId, pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a medical record by its ID")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        medicalRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Attachments
    @PostMapping("/{medicalRecordId}/attachments")
    @Operation(summary = "Add an attachment to a medical record")
    public ResponseEntity<AttachmentResponse> addAttachment(
            @PathVariable UUID medicalRecordId,
            @Parameter(description = "File to upload") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Attachment description") @RequestParam(value = "description", required = false) String description) {
        AttachmentRequest request = AttachmentRequest.builder().file(file).description(description).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecordService.addAttachment(medicalRecordId, request));
    }

    @DeleteMapping("/{medicalRecordId}/attachments/{attachmentId}")
    @Operation(summary = "Remove an attachment from a medical record")
    public ResponseEntity<Void> removeAttachment(@PathVariable UUID medicalRecordId, @PathVariable UUID attachmentId) {
        medicalRecordService.removeAttachment(medicalRecordId, attachmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{medicalRecordId}/attachments")
    @Operation(summary = "List all attachments for a medical record")
    public ResponseEntity<Page<AttachmentResponse>> getAttachments(@PathVariable UUID medicalRecordId, Pageable pageable) {
        return ResponseEntity.ok(medicalRecordService.getAttachments(medicalRecordId, pageable));
    }

    // Evolutions
    @PostMapping("/{medicalRecordId}/evolutions")
    @Operation(summary = "Add an evolution to a medical record")
    public ResponseEntity<EvolutionResponse> addEvolution(@PathVariable UUID medicalRecordId, @Valid @RequestBody EvolutionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecordService.addEvolution(medicalRecordId, request));
    }

    @GetMapping("/{medicalRecordId}/evolutions")
    @Operation(summary = "List all evolutions for a medical record")
    public ResponseEntity<Page<EvolutionResponse>> getEvolutions(@PathVariable UUID medicalRecordId, Pageable pageable) {
        return ResponseEntity.ok(medicalRecordService.getEvolutions(medicalRecordId, pageable));
    }
} 