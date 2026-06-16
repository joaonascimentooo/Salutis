package com.backend.dto;

import com.backend.domain.AddressType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PatientAdressResponse {

    private String id;
    private AddressType type;
    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;


    
}
