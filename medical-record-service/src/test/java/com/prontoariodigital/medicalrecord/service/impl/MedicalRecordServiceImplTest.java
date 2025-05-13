package com.prontoariodigital.medicalrecord.service.impl;

import com.prontoariodigital.medicalrecord.config.FileStorageConfig;
import com.prontoariodigital.medicalrecord.domain.Attachment;
import com.prontoariodigital.medicalrecord.domain.Evolution;
import com.prontoariodigital.medicalrecord.domain.MedicalRecord;
import com.prontoariodigital.medicalrecord.dto.*;
import com.prontoariodigital.medicalrecord.exception.ResourceNotFoundException;
import com.prontoariodigital.medicalrecord.repository.AttachmentRepository;
import com.prontoariodigital.medicalrecord.repository.EvolutionRepository;
import com.prontoariodigital.medicalrecord.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceImplTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private EvolutionRepository evolutionRepository;

    @Mock
    private FileStorageConfig fileStorageConfig;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    private UUID patientId;
    private UUID doctorId;
    private UUID medicalRecordId;
    private MedicalRecord medicalRecord;
    private MedicalRecordRequest medicalRecordRequest;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        doctorId = UUID.randomUUID();
        medicalRecordId = UUID.randomUUID();

        medicalRecord = MedicalRecord.builder()
                .id(medicalRecordId)
                .patientId(patientId)
                .doctorId(doctorId)
                .mainComplaint("Test complaint")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        medicalRecordRequest = new MedicalRecordRequest();
        medicalRecordRequest.setPatientId(patientId);
        medicalRecordRequest.setDoctorId(doctorId);
        medicalRecordRequest.setMainComplaint("Test complaint");
    }

    @Test
    void create_ShouldCreateMedicalRecord() {
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(medicalRecord);

        MedicalRecordResponse response = medicalRecordService.create(medicalRecordRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(medicalRecordId);
        assertThat(response.getPatientId()).isEqualTo(patientId);
        assertThat(response.getDoctorId()).isEqualTo(doctorId);
        verify(medicalRecordRepository).save(any(MedicalRecord.class));
    }

    @Test
    void update_ShouldUpdateMedicalRecord() {
        when(medicalRecordRepository.findById(medicalRecordId)).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(medicalRecord);

        MedicalRecordResponse response = medicalRecordService.update(medicalRecordId, medicalRecordRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(medicalRecordId);
        verify(medicalRecordRepository).findById(medicalRecordId);
        verify(medicalRecordRepository).save(any(MedicalRecord.class));
    }

    @Test
    void update_ShouldThrowException_WhenMedicalRecordNotFound() {
        when(medicalRecordRepository.findById(medicalRecordId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicalRecordService.update(medicalRecordId, medicalRecordRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Medical record not found");
    }

    @Test
    void findById_ShouldReturnMedicalRecord() {
        when(medicalRecordRepository.findById(medicalRecordId)).thenReturn(Optional.of(medicalRecord));

        MedicalRecordResponse response = medicalRecordService.findById(medicalRecordId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(medicalRecordId);
        verify(medicalRecordRepository).findById(medicalRecordId);
    }

    @Test
    void findById_ShouldThrowException_WhenMedicalRecordNotFound() {
        when(medicalRecordRepository.findById(medicalRecordId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicalRecordService.findById(medicalRecordId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Medical record not found");
    }

    @Test
    void findByPatientId_ShouldReturnPageOfMedicalRecords() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MedicalRecord> medicalRecordPage = new PageImpl<>(List.of(medicalRecord));
        when(medicalRecordRepository.findByPatientId(patientId, pageable)).thenReturn(medicalRecordPage);

        Page<MedicalRecordResponse> response = medicalRecordService.findByPatientId(patientId, pageable);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getId()).isEqualTo(medicalRecordId);
        verify(medicalRecordRepository).findByPatientId(patientId, pageable);
    }

    @Test
    void delete_ShouldDeleteMedicalRecord() {
        when(medicalRecordRepository.findById(medicalRecordId)).thenReturn(Optional.of(medicalRecord));
        doNothing().when(medicalRecordRepository).delete(medicalRecord);

        medicalRecordService.delete(medicalRecordId);

        verify(medicalRecordRepository).findById(medicalRecordId);
        verify(medicalRecordRepository).delete(medicalRecord);
    }

    @Test
    void addAttachment_ShouldAddAttachment() throws IOException {
        when(medicalRecordRepository.findById(medicalRecordId)).thenReturn(Optional.of(medicalRecord));
        when(fileStorageConfig.getUploadDir()).thenReturn("./uploads");
        when(attachmentRepository.save(any(Attachment.class))).thenAnswer(i -> i.getArgument(0));

        MultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );

        AttachmentRequest request = AttachmentRequest.builder()
                .file(file)
                .description("Test attachment")
                .build();

        AttachmentResponse response = medicalRecordService.addAttachment(medicalRecordId, request);

        assertThat(response).isNotNull();
        assertThat(response.getFileName()).contains("test.txt");
        assertThat(response.getDescription()).isEqualTo("Test attachment");
        verify(medicalRecordRepository).findById(medicalRecordId);
        verify(attachmentRepository).save(any(Attachment.class));
    }

    @Test
    void addEvolution_ShouldAddEvolution() {
        when(medicalRecordRepository.findById(medicalRecordId)).thenReturn(Optional.of(medicalRecord));
        when(evolutionRepository.save(any(Evolution.class))).thenAnswer(i -> i.getArgument(0));

        EvolutionRequest request = EvolutionRequest.builder()
                .medicalRecordId(medicalRecordId)
                .doctorId(doctorId)
                .description("Test evolution")
                .build();

        EvolutionResponse response = medicalRecordService.addEvolution(medicalRecordId, request);

        assertThat(response).isNotNull();
        assertThat(response.getDescription()).isEqualTo("Test evolution");
        assertThat(response.getDoctorId()).isEqualTo(doctorId);
        verify(medicalRecordRepository).findById(medicalRecordId);
        verify(evolutionRepository).save(any(Evolution.class));
    }
} 