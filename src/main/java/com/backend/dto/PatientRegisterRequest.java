package com.backend.dto;

import java.time.LocalDate;

import com.backend.domain.Gender;

import lombok.Data;

@Data
public class PatientRegisterRequest {

    private Gender gender;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String cpf;
    private String primaryPhone;
    
}
