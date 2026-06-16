package com.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {
    
    boolean existsByCpf(String cpf);
    boolean existsByLinkedUser_Id(String userId);
    Optional<Patient> findByLinkedUser_Email(String email);
}
