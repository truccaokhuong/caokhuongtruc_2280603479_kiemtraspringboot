package com.example.kiemtra.controller;

import com.example.kiemtra.dto.AppointmentForm;
import com.example.kiemtra.entity.Doctor;
import com.example.kiemtra.service.AppointmentService;
import com.example.kiemtra.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enroll")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @GetMapping("/doctor/{doctorId}")
    public String appointmentForm(@PathVariable Long doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointmentForm", new AppointmentForm());
        return "appointments/form";
    }

    @PostMapping("/doctor/{doctorId}")
    public String createAppointment(
            @PathVariable Long doctorId,
            @Valid @ModelAttribute("appointmentForm") AppointmentForm form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctor", doctor);
            return "appointments/form";
        }

        appointmentService.createAppointment(authentication.getName(), doctorId, form);
        redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully");
        return "redirect:/enroll/my-appointments";
    }

    @GetMapping("/my-appointments")
    public String myAppointments(Authentication authentication, Model model) {
        model.addAttribute("appointments", appointmentService.getMyAppointments(authentication.getName()));
        return "appointments/my-appointments";
    }
}
