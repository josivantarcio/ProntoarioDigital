package com.prontoariodigital.patient.controller;

import com.prontoariodigital.patient.dto.PatientRequest;
import com.prontoariodigital.patient.dto.PatientResponse;
import com.prontoariodigital.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "APIs de gerenciamento de pacientes")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "Cria um novo paciente", description = "Cadastra um novo paciente no sistema")
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um paciente", description = "Atualiza os dados de um paciente existente")
    public ResponseEntity<PatientResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um paciente", description = "Remove um paciente do sistema")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um paciente por ID", description = "Retorna os dados de um paciente específico")
    public ResponseEntity<PatientResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista pacientes", description = "Retorna uma lista paginada de pacientes")
    public ResponseEntity<Page<PatientResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(patientService.findAll(pageable));
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Busca um paciente por CPF", description = "Retorna os dados de um paciente pelo CPF")
    public ResponseEntity<PatientResponse> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(patientService.findByCpf(cpf));
    }
} 