package com.example.kiemtra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorForm {

    @NotBlank(message = "Doctor name is required")
    private String name;

    private String image;

    @NotBlank(message = "Specialty is required")
    private String specialty;

    @NotNull(message = "Department is required")
    private Long departmentId;
}
