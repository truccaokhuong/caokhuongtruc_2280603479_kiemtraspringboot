package com.example.kiemtra.service;

import com.example.kiemtra.dto.RegistrationForm;
import com.example.kiemtra.entity.Patient;
import com.example.kiemtra.entity.Role;
import com.example.kiemtra.repository.PatientRepository;
import com.example.kiemtra.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(
            PatientRepository patientRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerPatient(RegistrationForm form) {
        if (patientRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (patientRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new EntityNotFoundException("Role PATIENT not found"));

        Patient patient = Patient.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .email(form.getEmail())
                .roles(Set.of(patientRole))
                .build();

        patientRepository.save(patient);
    }

    public Patient findByUsername(String username) {
        return patientRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + username));
    }
}
