package com.example.kiemtra.repository;

import com.example.kiemtra.entity.Doctor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Override
    @EntityGraph(attributePaths = "department")
    Page<Doctor> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "department")
    Page<Doctor> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "department")
    Optional<Doctor> findById(Long id);
}
