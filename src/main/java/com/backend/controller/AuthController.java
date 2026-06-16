package com.backend.controller;

import com.backend.dto.AuthResponse;
import com.backend.dto.LoginRequest;
import com.backend.dto.RegisterRequest;
import com.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação e registro de usuários")
public class AuthController {

    private final AuthService authService;

  
    @PostMapping("/register")
    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário na plataforma"
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Nova requisição de registro para email: {}", registerRequest.getEmail());
                AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   
    @PostMapping("/login")
    @Operation(
            summary = "Fazer login",
            description = "Autentica um usuário e retorna um token JWT"
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Nova requisição de login para email: {}", loginRequest.getEmail());
                AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
