package com.backend.controller;

import com.backend.domain.User;
import com.backend.domain.UserRole;
import com.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Usuários", description = "Gerenciamento de usuários e perfis")
public class UserController {

    private final UserService userService;
    
    @GetMapping("/profile")
    @Operation(summary = "Obter perfil do usuário autenticado")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        String email = authentication.getName();
        log.info("Requisição de perfil para usuário: {}", email);
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obter usuário por ID (admin only)")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        log.info("Requisição de usuário: {}", id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

  
    @PostMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Alterar role de um usuário (admin only)")
    public ResponseEntity<User> updateUserRole(
            @PathVariable String id,
            @RequestParam UserRole role) {
        log.info("Atualizando role do usuário {} para {}", id, role);
        User updatedUser = userService.updateUserRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desativar usuário (admin only)")
    public ResponseEntity<Void> deactivateUser(@PathVariable String id) {
        log.info("Desativando usuário: {}", id);
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
}
