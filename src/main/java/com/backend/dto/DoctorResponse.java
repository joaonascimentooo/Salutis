package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DoctorResponse {

    private String id;
    private String crm;
    private String specialty;
    private String professionalTitle;
}
