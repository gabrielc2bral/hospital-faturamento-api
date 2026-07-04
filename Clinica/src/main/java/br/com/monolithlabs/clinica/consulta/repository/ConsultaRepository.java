package br.com.monolithlabs.clinica.consulta.repository;

import br.com.monolithlabs.clinica.consulta.entity.Consulta;
import br.com.monolithlabs.clinica.consulta.entity.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByMedicoIdAndDataAndStatus(Long medicoId, LocalDate data, StatusConsulta status);
    List<Consulta> findByPacienteIdOrderByDataDescHorarioDesc(Long pacienteId);
}
