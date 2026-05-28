package com.backend.dto;

import com.backend.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String userId;
    private String name;
    private String email;
    private UserRole role;
    private Boolean active;
}
