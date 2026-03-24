package com.example.kiemtra.controller;

import com.example.kiemtra.entity.Doctor;
import com.example.kiemtra.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final DoctorService doctorService;

    public HomeController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping({"/", "/home"})
    public String home(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Page<Doctor> doctorPage = doctorService.getDoctorPage(page, 5, keyword);
        model.addAttribute("doctorPage", doctorPage);
        model.addAttribute("keyword", keyword);
        return "home";
    }

    @GetMapping("/courses")
    public String courses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Page<Doctor> doctorPage = doctorService.getDoctorPage(page, 5, keyword);
        model.addAttribute("doctorPage", doctorPage);
        model.addAttribute("keyword", keyword);
        return "home";
    }
}
