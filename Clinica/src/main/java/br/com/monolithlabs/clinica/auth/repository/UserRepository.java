package br.com.monolithlabs.clinica.auth.repository;

import br.com.monolithlabs.clinica.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
