package com.prontoariodigital.patient.dto;

import com.prontoariodigital.patient.domain.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser uma data passada")
    private LocalDate birthDate;

    @NotBlank(message = "Gênero é obrigatório")
    @Pattern(regexp = "^(MASCULINO|FEMININO|OUTRO)$", message = "Gênero deve ser MASCULINO, FEMININO ou OUTRO")
    private String gender;

    @Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve conter 10 ou 11 dígitos numéricos")
    private String phoneNumber;

    @Email(message = "Email inválido")
    private String email;

    @Valid
    private Address address;
} 