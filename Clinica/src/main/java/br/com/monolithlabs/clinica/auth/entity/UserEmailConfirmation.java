package br.com.monolithlabs.clinica.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_email_confirmation")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
