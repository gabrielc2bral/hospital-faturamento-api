package com.gabriel.hospital.auth.repository;

import com.gabriel.hospital.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
