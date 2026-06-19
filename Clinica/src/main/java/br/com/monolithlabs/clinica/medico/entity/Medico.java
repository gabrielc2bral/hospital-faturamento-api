package br.com.monolithlabs.clinica.medico.entity;

import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.consulta.entity.Consulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "medico" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioTrabalhoMedico> horarioTrabalhoMedicoList;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String crm;

    @OneToMany(mappedBy = "medico")
    private List<Consulta> consultas;


    @ManyToOne
    @JoinColumn(name = "especialidade_id")
    private Especialidade especialidade;

}
