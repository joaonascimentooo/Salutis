package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.domain.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    boolean existsByUser_Id(String userId);
    boolean existsByCrm(String crm);
    
}
