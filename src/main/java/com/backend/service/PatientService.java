package com.backend.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.domain.Patient;
import com.backend.domain.User;
import com.backend.domain.UserRole;
import com.backend.dto.PatientRegisterRequest;
import com.backend.dto.PatientResponse;
import com.backend.repository.PatientRepository;
import com.backend.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    private final UserRepository userRepository;

    @Transactional
    public PatientResponse register(PatientRegisterRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);

        ensureUserDoesNotHavePatient(user.getId());

        ensureCpfIsAvailable(request.getCpf());

        user.setRole(UserRole.PATIENT);
        userRepository.save(user);

        
        Patient patient = Patient.builder()
                .gender(request.getGender())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .cpf(request.getCpf())
                .primaryPhone(request.getPrimaryPhone())
                .email(user.getEmail())
                .linkedUser(user)
                .build();

        Patient savedPatient = patientRepository.save(patient);

        log.info("Paciente cadastrado para o usuário {}", userEmail);

        return toResponse(savedPatient);
    }

    public PatientResponse getByUserEmail(String userEmail) {
        Patient patient = patientRepository.findByLinkedUser_Email(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Paciente não encontrado para o usuário: " + userEmail
                ));

        return toResponse(patient);
    }
    
    
    private User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + userEmail
                ));
    }

     private void ensureUserDoesNotHavePatient(String userId) {
        if (patientRepository.existsByLinkedUser_Id(userId)) {
            throw new IllegalArgumentException("Esse usuário já possui cadastro de paciente");
        }
    }

     private void ensureCpfIsAvailable(String cpf) {
        if (patientRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado para outro paciente");
        }
    }

    private PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .gender(patient.getGender())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .birthDate(patient.getBirthDate())
                .primaryPhone(patient.getPrimaryPhone())
                .email(patient.getEmail())
                .build();
    }
}
