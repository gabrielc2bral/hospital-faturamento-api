package br.com.monolithlabs.clinica.paciente.entity;

import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.consulta.entity.Consulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pacientes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;

    private String CPF;

    private LocalDate dataNascimento;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas;
}
