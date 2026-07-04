package br.com.monolithlabs.clinica.paciente.repository;

import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.paciente.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByUser(User user);
}
