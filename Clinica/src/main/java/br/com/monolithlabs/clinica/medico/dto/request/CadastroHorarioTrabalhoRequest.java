package br.com.monolithlabs.clinica.medico.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroHorarioTrabalhoRequest {
    @NotNull(message = "Dia da semana é obrigatório")
    private DayOfWeek diaDaSemana;

    @NotNull(message = "Hora de início é obrigatória")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaFim;
}
