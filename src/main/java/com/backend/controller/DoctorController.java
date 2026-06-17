package com.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.backend.dto.DoctorRequest;
import com.backend.dto.DoctorResponse;
import com.backend.service.DoctorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "doctors", description = "Cadastro e consulta do perfil do paciente" )
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(
        summary = "Cadastrar Médico",
        description = "Criar o perfil do médico  vinculado ao usuário autenticado"
    )
    public ResponseEntity<DoctorResponse> register(@Valid @RequestBody DoctorRequest request, Authentication authentication ){
        String userEmail = authentication.getName();

        log.info("Cadastro de médico solicitado pelo usuário: {}", userEmail);

        DoctorResponse response = doctorService.register(request, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
         @GetMapping("/me")
         @Operation(
            summary = "Buscar Médico",
            description = "Retornar o médico vinculado ao usuario autenticado"
         )
         public ResponseEntity<DoctorResponse> getMyDoctor(Authentication authentication){
            String userEmail = authentication.getName();
                log.info("Consulta de médico solicitada pelo usuário: {}", userEmail);

                DoctorResponse response = doctorService.getByUserEmail(userEmail);

            return ResponseEntity.ok(response);
         }
}
