package br.com.monolithlabs.clinica.medico.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarMedicoRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CRM é obrigatório")
    private String crm;

    @NotNull(message = "Especialidade é obrigatória")
    private Long especialidadeId;
}
