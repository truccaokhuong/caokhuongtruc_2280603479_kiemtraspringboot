package com.example.kiemtra.controller;

import com.example.kiemtra.dto.DoctorForm;
import com.example.kiemtra.entity.Doctor;
import com.example.kiemtra.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/doctors")
public class AdminDoctorController {

    private final DoctorService doctorService;

    public AdminDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public String listDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Page<Doctor> doctorPage = doctorService.getDoctorPage(page, 5, keyword);
        model.addAttribute("doctorPage", doctorPage);
        model.addAttribute("keyword", keyword);
        return "admin/doctors/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("doctorForm", new DoctorForm());
        model.addAttribute("departments", doctorService.getAllDepartments());
        model.addAttribute("formTitle", "Create Doctor");
        model.addAttribute("actionUrl", "/admin/doctors/create");
        return "admin/doctors/form";
    }

    @PostMapping("/create")
    public String createDoctor(
            @Valid @ModelAttribute("doctorForm") DoctorForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", doctorService.getAllDepartments());
            model.addAttribute("formTitle", "Create Doctor");
            model.addAttribute("actionUrl", "/admin/doctors/create");
            return "admin/doctors/form";
        }

        doctorService.createDoctor(form);
        redirectAttributes.addFlashAttribute("successMessage", "Doctor created successfully");
        return "redirect:/admin/doctors";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        model.addAttribute("doctorForm", doctorService.toForm(doctor));
        model.addAttribute("departments", doctorService.getAllDepartments());
        model.addAttribute("formTitle", "Update Doctor");
        model.addAttribute("actionUrl", "/admin/doctors/edit/" + id);
        return "admin/doctors/form";
    }

    @PostMapping("/edit/{id}")
    public String updateDoctor(
            @PathVariable Long id,
            @Valid @ModelAttribute("doctorForm") DoctorForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", doctorService.getAllDepartments());
            model.addAttribute("formTitle", "Update Doctor");
            model.addAttribute("actionUrl", "/admin/doctors/edit/" + id);
            return "admin/doctors/form";
        }

        doctorService.updateDoctor(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Doctor updated successfully");
        return "redirect:/admin/doctors";
    }

    @PostMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        doctorService.deleteDoctor(id);
        redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully");
        return "redirect:/admin/doctors";
    }
}
