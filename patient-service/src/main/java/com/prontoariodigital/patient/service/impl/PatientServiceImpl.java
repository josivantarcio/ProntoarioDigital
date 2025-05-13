package com.prontoariodigital.patient.service.impl;

import com.prontoariodigital.patient.domain.Patient;
import com.prontoariodigital.patient.dto.PatientRequest;
import com.prontoariodigital.patient.dto.PatientResponse;
import com.prontoariodigital.patient.repository.PatientRepository;
import com.prontoariodigital.patient.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientResponse create(PatientRequest request) {
        if (patientRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        Patient patient = Patient.builder()
            .name(request.getName())
            .cpf(request.getCpf())
            .birthDate(request.getBirthDate())
            .gender(request.getGender())
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .address(request.getAddress())
            .build();

        patient = patientRepository.save(patient);
        return mapToResponse(patient);
    }

    @Override
    @Transactional
    public PatientResponse update(UUID id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (!patient.getCpf().equals(request.getCpf()) && patientRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        patient.setName(request.getName());
        patient.setCpf(request.getCpf());
        patient.setBirthDate(request.getBirthDate());
        patient.setGender(request.getGender());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());

        patient = patientRepository.save(patient);
        return mapToResponse(patient);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Paciente não encontrado");
        }
        patientRepository.deleteById(id);
    }

    @Override
    public PatientResponse findById(UUID id) {
        return patientRepository.findById(id)
            .map(this::mapToResponse)
            .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
    }

    @Override
    public Page<PatientResponse> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable)
            .map(this::mapToResponse);
    }

    @Override
    public PatientResponse findByCpf(String cpf) {
        return patientRepository.findByCpf(cpf)
            .map(this::mapToResponse)
            .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
    }

    private PatientResponse mapToResponse(Patient patient) {
        return PatientResponse.builder()
            .id(patient.getId())
            .name(patient.getName())
            .cpf(patient.getCpf())
            .birthDate(patient.getBirthDate())
            .gender(patient.getGender())
            .phoneNumber(patient.getPhoneNumber())
            .email(patient.getEmail())
            .address(patient.getAddress())
            .createdAt(patient.getCreatedAt())
            .updatedAt(patient.getUpdatedAt())
            .build();
    }
} 