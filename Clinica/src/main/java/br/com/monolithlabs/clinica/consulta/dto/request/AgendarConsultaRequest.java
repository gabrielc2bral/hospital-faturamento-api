package br.com.monolithlabs.clinica.consulta.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendarConsultaRequest {
    @NotNull(message = "Médico é obrigatório")
    private Long medicoId;

    @NotNull(message = "Data é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    @NotNull(message = "Horário é obrigatório")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horario;
}
