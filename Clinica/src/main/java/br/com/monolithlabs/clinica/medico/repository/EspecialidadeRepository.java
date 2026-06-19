package br.com.monolithlabs.clinica.medico.repository;

import br.com.monolithlabs.clinica.medico.entity.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
}
