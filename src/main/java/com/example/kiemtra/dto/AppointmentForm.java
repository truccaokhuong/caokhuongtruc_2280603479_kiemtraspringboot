package com.example.kiemtra.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class AppointmentForm {

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date must be today or later")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
}
