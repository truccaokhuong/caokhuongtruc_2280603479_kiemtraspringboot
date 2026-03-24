package com.example.kiemtra.repository;

import com.example.kiemtra.entity.Appointment;
import com.example.kiemtra.entity.Patient;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	@EntityGraph(attributePaths = {"doctor", "doctor.department"})
	List<Appointment> findByPatientOrderByAppointmentDateDesc(Patient patient);
}
