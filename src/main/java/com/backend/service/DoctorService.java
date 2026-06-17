package com.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domain.Doctor;
import com.backend.domain.User;
import com.backend.domain.UserRole;
import com.backend.dto.DoctorRequest;
import com.backend.dto.DoctorResponse;
import com.backend.dto.DoctorUpdateRequest;
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

        user.setRole(UserRole.DOCTOR);
        userRepository.save(user);

        Doctor doctor = Doctor.builder()
            .crm(request.getCrm())
            .specialty(request.getSpecialty())
            .professionalTitle(request.getProfessionalTitle())
            .user(user)
            .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return toResponse(savedDoctor);
    }

    @Transactional
    public DoctorResponse updateMyDoctor(DoctorUpdateRequest request, String userEmail) {
        Doctor doctor = getDoctorByUserEmail(userEmail);

        if (request.getCrm() != null && !request.getCrm().equals(doctor.getCrm())) {
            ensureCrmIsAvailableForUpdate(request.getCrm(), doctor.getId());
            doctor.setCrm(request.getCrm());
        }

        if (request.getSpecialty() != null) {
            doctor.setSpecialty(request.getSpecialty());
        }

        if (request.getProfessionalTitle() != null) {
            doctor.setProfessionalTitle(request.getProfessionalTitle());
        }
        doctor.setUpdatedAt(LocalDateTime.now());

        Doctor savedDoctor = doctorRepository.save(doctor);

        return toResponse(savedDoctor);
    }

    public DoctorResponse getByUserEmail(String userEmail) {
        Doctor doctor = getDoctorByUserEmail(userEmail);

        return toResponse(doctor);
    }

    public List<DoctorResponse> listAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
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

    private void ensureCrmIsAvailableForUpdate(String crm, String doctorId) {
        if (doctorRepository.existsByCrmAndIdNot(crm, doctorId)) {
            throw new IllegalArgumentException("CRM já cadastrado para outro medico");
        }
    }

    private User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + userEmail
                ));
    }

    private Doctor getDoctorByUserEmail(String userEmail) {
        return doctorRepository.findByUser_Email(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Médico não encontrado para o usuário: " + userEmail
                ));
    }

    private DoctorResponse toResponse(Doctor doctor){
        return DoctorResponse.builder()
        .id(doctor.getId())
        .crm(doctor.getCrm())
        .specialty(doctor.getSpecialty())
        .professionalTitle(doctor.getProfessionalTitle())
        .build();
    }
}
