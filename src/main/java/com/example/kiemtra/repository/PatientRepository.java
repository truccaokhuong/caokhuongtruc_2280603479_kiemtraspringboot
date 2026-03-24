package com.example.kiemtra.repository;

import com.example.kiemtra.entity.Patient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUsername(String username);

    Optional<Patient> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
