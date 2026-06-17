package com.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.domain.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    boolean existsByUser_Id(String userId);
    boolean existsByCrm(String crm);
    boolean existsByCrmAndIdNot(String crm, String id);
    Optional<Doctor> findByUser_Email(String email);
    
}
