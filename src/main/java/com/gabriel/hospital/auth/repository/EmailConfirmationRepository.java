package com.gabriel.hospital.auth.repository;

import com.gabriel.hospital.auth.entity.UserEmailConfirmation;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmailConfirmationRepository extends JpaRepository<UserEmailConfirmation, Long> {
    Optional<UserEmailConfirmation> findByEmail(String email);
}
