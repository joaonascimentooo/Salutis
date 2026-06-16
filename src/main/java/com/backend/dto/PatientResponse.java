package com.backend.dto;

import java.time.LocalDate;

import com.backend.domain.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PatientResponse {

    private String id;
    private Gender gender;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String primaryPhone;
    private String email;
}
