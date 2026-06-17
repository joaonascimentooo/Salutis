package com.backend.dto;

import lombok.Data;

@Data
public class DoctorUpdateRequest {

    private String crm;
    private String specialty;
    private String professionalTitle;
}