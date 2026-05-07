package com.gabriel.hospital.auth.controller;

import com.gabriel.hospital.paciente.dto.PacienteDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class cadastroController {
    @PostMapping("/cadastro")
    public String salvar(@ModelAttribute PacienteDTO dto) {

        System.out.println(dto.getNomeCompleto());
        System.out.println(dto.getDataNascimento());
        System.out.println(dto.getCpf());
        System.out.println(dto.getEmail());
        System.out.println(dto.getSenha());

        return "home/index";
    }
}
