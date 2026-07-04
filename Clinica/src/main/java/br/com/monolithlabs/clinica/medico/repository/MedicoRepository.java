package br.com.monolithlabs.clinica.medico.repository;

import br.com.monolithlabs.clinica.medico.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    long countByEspecialidadeId(Long especialidadeId);
    List<Medico> findByEspecialidadeId(Long especialidadeId);
}
