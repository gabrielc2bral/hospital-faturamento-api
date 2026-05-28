package com.gabriel.hospital.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_email_confirmation")
@Data
public class UserEmailConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String email;

    private LocalDateTime expiresAt;

    private boolean used;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
