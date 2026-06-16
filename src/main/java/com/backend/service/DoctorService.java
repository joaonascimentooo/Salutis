package com.backend.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domain.Doctor;
import com.backend.domain.User;
import com.backend.dto.DoctorRequest;
import com.backend.dto.DoctorResponse;
import com.backend.repository.DoctorRepository;
import com.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public DoctorResponse register(DoctorRequest request, String userEmail){
        User user = getUserByEmail(userEmail);

        ensureUserDoesNotHaveDoctor(user.getId());
        ensureCrmIsAvailable(request.getCrm());

        Doctor doctor = Doctor.builder()
            .crm(request.getCrm())
            .specialty(request.getSpecialty())
            .professionalTitle(request.getProfessionalTitle())
            .user(user)
            .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return toResponse(savedDoctor);
    }

    private void ensureUserDoesNotHaveDoctor(String userId){
        if (doctorRepository.existsByUser_Id(userId)) {
            throw new IllegalArgumentException("Esse usuário já possui cadastro de doutor");
        }
    } 

    private void ensureCrmIsAvailable(String crm){
        if (doctorRepository.existsByCrm(crm)) {
            throw new IllegalArgumentException("CRM já cadastrado para outro medico");
        }
    }

    private User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + userEmail
                ));
    }

    private DoctorResponse toResponse(Doctor doctor){
        return DoctorResponse.builder()
        .id(doctor.getId())
        .crm(doctor.getCrm())
        .professionalTitle(doctor.getProfessionalTitle())
        .build();
    }
}
