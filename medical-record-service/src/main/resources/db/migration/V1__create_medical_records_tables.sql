CREATE TABLE medical_records (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    doctor_id UUID NOT NULL,
    main_complaint VARCHAR(255) NOT NULL,
    current_disease_history TEXT,
    past_disease_history TEXT,
    family_history TEXT,
    physical_examination TEXT,
    diagnosis TEXT,
    treatment TEXT,
    evolution TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE attachments (
    id UUID PRIMARY KEY,
    medical_record_id UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (medical_record_id) REFERENCES medical_records(id) ON DELETE CASCADE
);

CREATE TABLE evolutions (
    id UUID PRIMARY KEY,
    medical_record_id UUID NOT NULL,
    doctor_id UUID NOT NULL,
    description TEXT NOT NULL,
    evolution_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (medical_record_id) REFERENCES medical_records(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_medical_records_patient_id ON medical_records(patient_id);
CREATE INDEX idx_medical_records_doctor_id ON medical_records(doctor_id);
CREATE INDEX idx_attachments_medical_record_id ON attachments(medical_record_id);
CREATE INDEX idx_evolutions_medical_record_id ON evolutions(medical_record_id);
CREATE INDEX idx_evolutions_doctor_id ON evolutions(doctor_id); 