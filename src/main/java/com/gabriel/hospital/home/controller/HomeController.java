package com.gabriel.hospital.home.controller;

import com.gabriel.hospital.paciente.dto.PacienteDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home/index";
    }

}
