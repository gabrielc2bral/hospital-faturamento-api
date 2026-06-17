package br.com.monolithlabs.clinica.paciente.service;

import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.paciente.dto.PacienteDTO;
import br.com.monolithlabs.clinica.paciente.entity.Paciente;
import br.com.monolithlabs.clinica.paciente.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    public void concluirCadastro(PacienteDTO dto, User userLogado) {
        Paciente paciente = new Paciente();
        paciente.setCPF(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setNomeCompleto(dto.getNomeCompleto());
        paciente.setUser(userLogado);
        pacienteRepository.save(paciente);
    }
}
