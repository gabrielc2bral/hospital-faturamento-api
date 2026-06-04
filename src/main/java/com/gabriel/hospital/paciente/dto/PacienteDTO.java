package com.gabriel.hospital.paciente.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacienteDTO {
    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private String senha;

}
