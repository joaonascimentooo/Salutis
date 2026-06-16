package com.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.PatientRegisterRequest;
import com.backend.dto.PatientResponse;
import com.backend.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Cadastro e consulta do perfil do paciente")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(
            summary = "Cadastrar paciente",
            description = "Cria o perfil do paciente vinculado ao usuário autenticado"
    )
    public ResponseEntity<PatientResponse> register(
            @Valid @RequestBody PatientRegisterRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();

        log.info("Cadastro de paciente solicitado pelo usuário: {}", userEmail);

        PatientResponse response = patientService.register(request, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Buscar meu paciente",
            description = "Retorna o paciente vinculado ao usuário autenticado"
    )
    public ResponseEntity<PatientResponse> getMyPatient(Authentication authentication) {
        String userEmail = authentication.getName();

        log.info("Consulta do paciente do usuário: {}", userEmail);

        PatientResponse response = patientService.getByUserEmail(userEmail);

        return ResponseEntity.ok(response);
    }
}