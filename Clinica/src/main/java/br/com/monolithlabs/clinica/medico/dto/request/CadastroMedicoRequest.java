package br.com.monolithlabs.clinica.medico.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroMedicoRequest {
    @NotBlank
    @Email
    private String email;
    @NotNull
    private String nome;
    @NotNull
    private Long especialidadeId;
    @NotBlank
    private String crm;
}
