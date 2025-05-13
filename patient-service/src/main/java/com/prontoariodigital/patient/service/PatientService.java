package com.prontoariodigital.patient.service;

import com.prontoariodigital.patient.dto.PatientRequest;
import com.prontoariodigital.patient.dto.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PatientService {
    PatientResponse create(PatientRequest request);
    PatientResponse update(UUID id, PatientRequest request);
    void delete(UUID id);
    PatientResponse findById(UUID id);
    Page<PatientResponse> findAll(Pageable pageable);
    PatientResponse findByCpf(String cpf);
} 