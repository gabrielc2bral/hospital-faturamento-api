package br.com.monolithlabs.clinica.auth.repository;

import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.auth.entity.UserEmailConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
