package com.example.kiemtra.service;

import com.example.kiemtra.dto.DoctorForm;
import com.example.kiemtra.entity.Department;
import com.example.kiemtra.entity.Doctor;
import com.example.kiemtra.repository.DepartmentRepository;
import com.example.kiemtra.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorService(DoctorRepository doctorRepository, DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    public Page<Doctor> getDoctorPage(int page, int size) {
        return getDoctorPage(page, size, null);
    }

    public Page<Doctor> getDoctorPage(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        if (keyword != null && !keyword.isBlank()) {
            return doctorRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        }
        return doctorRepository.findAll(pageable);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll(Sort.by("name").ascending());
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found: " + id));
    }

    @Transactional
    public Doctor createDoctor(DoctorForm form) {
        Department department = findDepartment(form.getDepartmentId());

        Doctor doctor = Doctor.builder()
                .name(form.getName())
                .image(form.getImage())
                .specialty(form.getSpecialty())
                .department(department)
                .build();

        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor updateDoctor(Long id, DoctorForm form) {
        Doctor doctor = getDoctorById(id);
        Department department = findDepartment(form.getDepartmentId());

        doctor.setName(form.getName());
        doctor.setImage(form.getImage());
        doctor.setSpecialty(form.getSpecialty());
        doctor.setDepartment(department);

        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor not found: " + id);
        }
        doctorRepository.deleteById(id);
    }

    public DoctorForm toForm(Doctor doctor) {
        DoctorForm form = new DoctorForm();
        form.setName(doctor.getName());
        form.setImage(doctor.getImage());
        form.setSpecialty(doctor.getSpecialty());
        form.setDepartmentId(doctor.getDepartment().getId());
        return form;
    }

    private Department findDepartment(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + departmentId));
    }
}
