package br.com.monolithlabs.clinica.medico.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroEspecialidadeRequest {
    @NotBlank
    private String nome;
}
