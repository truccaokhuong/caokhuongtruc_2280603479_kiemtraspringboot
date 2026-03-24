package com.example.kiemtra.service;

import com.example.kiemtra.dto.AppointmentForm;
import com.example.kiemtra.entity.Appointment;
import com.example.kiemtra.entity.Doctor;
import com.example.kiemtra.entity.Patient;
import com.example.kiemtra.repository.AppointmentRepository;
import com.example.kiemtra.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientService patientService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientService patientService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
    }

    public List<Appointment> getMyAppointments(String username) {
        Patient patient = patientService.findByUsername(username);
        return appointmentRepository.findByPatientOrderByAppointmentDateDesc(patient);
    }

    @Transactional
    public void createAppointment(String username, Long doctorId, AppointmentForm form) {
        Patient patient = patientService.findByUsername(username);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found: " + doctorId));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(form.getAppointmentDate())
                .build();

        appointmentRepository.save(appointment);
    }
}
