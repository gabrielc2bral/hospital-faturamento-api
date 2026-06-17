package br.com.monolithlabs.clinica.paciente.repository;

import br.com.monolithlabs.clinica.paciente.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
