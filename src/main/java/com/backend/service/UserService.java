package com.backend.service;

import com.backend.domain.User;
import com.backend.domain.UserRole;
import com.backend.dto.AuthResponse;
import com.backend.dto.LoginRequest;
import com.backend.dto.RegisterRequest;
import com.backend.repository.UserRepository;
import com.backend.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

  
    public UserService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      @Lazy AuthenticationManager authenticationManager,
                      JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

   
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", email);
                    return new UsernameNotFoundException("Usuário não encontrado: " + email);
                });
    }

    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        
        validatePasswordStrength(request.getPassword());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email já cadastrado: {}", request.getEmail());
            throw new IllegalArgumentException("Email já está registrado");
        }

        
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.PATIENT) 
                .active(true)
                .build();

        userRepository.save(user);
        log.info("Novo usuário registrado: {}", user.getEmail());

        
        String token = jwtUtil.generateTokenFromEmail(user.getEmail());
        return buildAuthResponse(user, token);
    }

  
    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 8 caracteres");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos 1 letra MAIÚSCULA");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos 1 letra minúscula");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos 1 número");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos 1 caractere especial (!@#$%^&*)");
        }
    }

    
    public AuthResponse login(LoginRequest request) {
        try {
           
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtUtil.generateToken(authentication);

            log.info("Usuário autenticado com sucesso: {}", user.getEmail());
            return buildAuthResponse(user, token);

        } catch (Exception e) {
            log.error("Falha na autenticação: {}", e.getMessage());
            throw new IllegalArgumentException("Email ou senha inválidos");
        }
    }

   
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + id));
    }

   
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    
    @Transactional
    public User updateUserRole(String userId, UserRole newRole) {
        User user = getUserById(userId);
        user.setRole(newRole);
        userRepository.save(user);
        log.info("Role do usuário {} atualizado para {}", userId, newRole);
        return user;
    }

    
    @Transactional
    public void deactivateUser(String userId) {
        User user = getUserById(userId);
        user.setActive(false);
        userRepository.save(user);
        log.info("Usuário desativado: {}", userId);
    }

   
    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }
}
