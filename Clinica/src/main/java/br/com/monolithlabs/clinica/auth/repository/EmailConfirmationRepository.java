package br.com.monolithlabs.clinica.auth.repository;


import br.com.monolithlabs.clinica.auth.entity.UserEmailConfirmation;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmationRepository extends JpaRepository<UserEmailConfirmation, Long> {
    Optional<UserEmailConfirmation> findByEmailAndUsedFalse(String email);
}
