package com.prontoariodigital.medicalrecord.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prontoariodigital.medicalrecord.config.FileStorageConfig;
import com.prontoariodigital.medicalrecord.config.TestSecurityConfig;
import com.prontoariodigital.medicalrecord.domain.Attachment;
import com.prontoariodigital.medicalrecord.domain.Evolution;
import com.prontoariodigital.medicalrecord.domain.MedicalRecord;
import com.prontoariodigital.medicalrecord.dto.*;
import com.prontoariodigital.medicalrecord.repository.AttachmentRepository;
import com.prontoariodigital.medicalrecord.repository.EvolutionRepository;
import com.prontoariodigital.medicalrecord.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private EvolutionRepository evolutionRepository;

    @Autowired
    private FileStorageConfig fileStorageConfig;

    private UUID patientId;
    private UUID doctorId;
    private UUID medicalRecordId;
    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        medicalRecordRepository.deleteAll();
        attachmentRepository.deleteAll();
        evolutionRepository.deleteAll();

        patientId = UUID.randomUUID();
        doctorId = UUID.randomUUID();
        medicalRecordId = UUID.randomUUID();

        LocalDateTime now = LocalDateTime.now();
        medicalRecord = MedicalRecord.builder()
                .id(medicalRecordId)
                .patientId(patientId)
                .doctorId(doctorId)
                .mainComplaint("Test complaint")
                .currentDiseaseHistory(null)
                .pastDiseaseHistory(null)
                .familyHistory(null)
                .physicalExamination(null)
                .diagnosis(null)
                .treatment(null)
                .evolution(null)
                .attachments(List.of())
                .evolutions(List.of())
                .createdAt(now)
                .updatedAt(now)
                .build();
        medicalRecordRepository.save(medicalRecord);
    }

    @Test
    void create_ShouldCreateMedicalRecord() throws Exception {
        MedicalRecordRequest request = MedicalRecordRequest.builder()
                .patientId(patientId)
                .doctorId(doctorId)
                .mainComplaint("New complaint")
                .currentDiseaseHistory(null)
                .pastDiseaseHistory(null)
                .familyHistory(null)
                .physicalExamination(null)
                .diagnosis(null)
                .treatment(null)
                .evolution(null)
                .build();

        mockMvc.perform(post("/api/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.patientId").value(patientId.toString()))
                .andExpect(jsonPath("$.doctorId").value(doctorId.toString()))
                .andExpect(jsonPath("$.mainComplaint").value("New complaint"));
    }

    @Test
    void update_ShouldUpdateMedicalRecord() throws Exception {
        MedicalRecordRequest request = MedicalRecordRequest.builder()
                .patientId(patientId)
                .doctorId(doctorId)
                .mainComplaint("Updated complaint")
                .currentDiseaseHistory(null)
                .pastDiseaseHistory(null)
                .familyHistory(null)
                .physicalExamination(null)
                .diagnosis(null)
                .treatment(null)
                .evolution(null)
                .build();

        mockMvc.perform(put("/api/medical-records/{id}", medicalRecordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(medicalRecordId.toString()))
                .andExpect(jsonPath("$.mainComplaint").value("Updated complaint"));
    }

    @Test
    void findById_ShouldReturnMedicalRecord() throws Exception {
        mockMvc.perform(get("/api/medical-records/{id}", medicalRecordId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(medicalRecordId.toString()))
                .andExpect(jsonPath("$.patientId").value(patientId.toString()))
                .andExpect(jsonPath("$.doctorId").value(doctorId.toString()));
    }

    @Test
    void findByPatientId_ShouldReturnPageOfMedicalRecords() throws Exception {
        mockMvc.perform(get("/api/medical-records/patient/{patientId}", patientId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(medicalRecordId.toString()));
    }

    @Test
    void delete_ShouldDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/api/medical-records/{id}", medicalRecordId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/medical-records/{id}", medicalRecordId))
                .andExpect(status().isNotFound());
    }

    @Test
    void addAttachment_ShouldAddAttachment() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/medical-records/{id}/attachments", medicalRecordId)
                        .file(file)
                        .param("description", "Test attachment"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fileName").exists())
                .andExpect(jsonPath("$.description").value("Test attachment"));
    }

    @Test
    void addEvolution_ShouldAddEvolution() throws Exception {
        EvolutionRequest request = EvolutionRequest.builder()
                .medicalRecordId(medicalRecordId)
                .doctorId(doctorId)
                .description("Test evolution")
                .build();

        mockMvc.perform(post("/api/medical-records/{id}/evolutions", medicalRecordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test evolution"))
                .andExpect(jsonPath("$.doctorId").value(doctorId.toString()));
    }

    @Test
    void getAttachments_ShouldReturnPageOfAttachments() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Attachment attachment = Attachment.builder()
                .medicalRecord(medicalRecord)
                .fileName("test.txt")
                .fileType("text/plain")
                .filePath("/tmp/test.txt")
                .description("Test attachment")
                .createdAt(now)
                .updatedAt(now)
                .build();
        attachmentRepository.save(attachment);

        mockMvc.perform(get("/api/medical-records/{id}/attachments", medicalRecordId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].fileName").value("test.txt"));
    }

    @Test
    void getEvolutions_ShouldReturnPageOfEvolutions() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Evolution evolution = Evolution.builder()
                .medicalRecord(medicalRecord)
                .doctorId(doctorId)
                .description("Test evolution")
                .evolutionDate(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
        evolutionRepository.save(evolution);

        mockMvc.perform(get("/api/medical-records/{id}/evolutions", medicalRecordId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].description").value("Test evolution"));
    }
} 