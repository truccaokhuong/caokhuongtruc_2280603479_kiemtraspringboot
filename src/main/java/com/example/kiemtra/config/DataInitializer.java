package com.example.kiemtra.config;

import com.example.kiemtra.entity.Department;
import com.example.kiemtra.entity.Doctor;
import com.example.kiemtra.entity.Patient;
import com.example.kiemtra.entity.Role;
import com.example.kiemtra.repository.DepartmentRepository;
import com.example.kiemtra.repository.DoctorRepository;
import com.example.kiemtra.repository.PatientRepository;
import com.example.kiemtra.repository.RoleRepository;
import java.util.List;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            RoleRepository roleRepository,
            PatientRepository patientRepository,
            DepartmentRepository departmentRepository,
            DoctorRepository doctorRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));
            Role patientRole = roleRepository.findByName("PATIENT")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("PATIENT").build()));

            if (patientRepository.findByUsername("admin").isEmpty()) {
                patientRepository.save(Patient.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@clinic.local")
                        .roles(Set.of(adminRole))
                        .build());
            }

            if (patientRepository.findByUsername("patient").isEmpty()) {
                patientRepository.save(Patient.builder()
                        .username("patient")
                        .password(passwordEncoder.encode("patient123"))
                        .email("patient@clinic.local")
                        .roles(Set.of(patientRole))
                        .build());
            }

            if (departmentRepository.count() == 0) {
                Department cardiology = departmentRepository.save(Department.builder().name("Cardiology").build());
                Department neurology = departmentRepository.save(Department.builder().name("Neurology").build());
                Department pediatrics = departmentRepository.save(Department.builder().name("Pediatrics").build());
                Department orthopedics = departmentRepository.save(Department.builder().name("Orthopedics").build());

                List<Doctor> sampleDoctors = List.of(
                        Doctor.builder().name("Dr. Minh Tran").specialty("Heart Failure").department(cardiology).image("https://images.unsplash.com/photo-1537368910025-700350fe46c7?w=300").build(),
                        Doctor.builder().name("Dr. Lan Nguyen").specialty("Interventional Cardiology").department(cardiology).image("https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=300").build(),
                        Doctor.builder().name("Dr. Quang Vo").specialty("Stroke Care").department(neurology).image("https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=300").build(),
                        Doctor.builder().name("Dr. Hanh Le").specialty("Epilepsy").department(neurology).image("https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=300").build(),
                        Doctor.builder().name("Dr. Thu Pham").specialty("Neonatology").department(pediatrics).image("https://images.unsplash.com/photo-1651008376811-b90baee60c1f?w=300").build(),
                        Doctor.builder().name("Dr. Dung Hoang").specialty("Child Nutrition").department(pediatrics).image("https://images.unsplash.com/photo-1594824476967-48c8b964273f?w=300").build(),
                        Doctor.builder().name("Dr. Bao Do").specialty("Joint Replacement").department(orthopedics).image("https://images.unsplash.com/photo-1622902046580-2b47f47f5471?w=300").build(),
                        Doctor.builder().name("Dr. Huong Bui").specialty("Sports Injury").department(orthopedics).image("https://images.unsplash.com/photo-1584467735871-8f27a2156341?w=300").build(),
                        Doctor.builder().name("Dr. Tuan Dang").specialty("Cardiac Imaging").department(cardiology).image("https://images.unsplash.com/photo-1580281657521-47f249e8f4df?w=300").build(),
                        Doctor.builder().name("Dr. Nga Tran").specialty("Headache Clinic").department(neurology).image("https://images.unsplash.com/photo-1666214280391-8ff5bd3c0bf0?w=300").build(),
                        Doctor.builder().name("Dr. Son Pham").specialty("Pediatric Allergy").department(pediatrics).image("https://images.unsplash.com/photo-1591604466107-ec97de577aff?w=300").build(),
                        Doctor.builder().name("Dr. Nhi Le").specialty("Spine Surgery").department(orthopedics).image("https://images.unsplash.com/photo-1631217868264-e5b90bb7e133?w=300").build()
                );

                doctorRepository.saveAll(sampleDoctors);
            }
        };
    }
}
