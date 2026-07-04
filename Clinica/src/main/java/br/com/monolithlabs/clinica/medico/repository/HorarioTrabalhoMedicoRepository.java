package br.com.monolithlabs.clinica.medico.repository;

import br.com.monolithlabs.clinica.medico.entity.HorarioTrabalhoMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface HorarioTrabalhoMedicoRepository extends JpaRepository<HorarioTrabalhoMedico, Long> {
    List<HorarioTrabalhoMedico> findByMedicoIdOrderByDiaDaSemanaAscHoraInicioAsc(Long medicoId);
    List<HorarioTrabalhoMedico> findByMedicoIdAndDiaDaSemana(Long medicoId, DayOfWeek diaDaSemana);
}
