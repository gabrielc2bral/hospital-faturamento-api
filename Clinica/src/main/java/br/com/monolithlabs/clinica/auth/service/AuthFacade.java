package br.com.monolithlabs.clinica.auth.service;

import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.paciente.dto.PacienteDTO;
import br.com.monolithlabs.clinica.paciente.service.PacienteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {
    private final UserService userService;
    private final PacienteService pacienteService;
    private final SecurityService securityService;
    @Transactional
    public void concluirCadastroFacade(PacienteDTO paciente, User user, HttpServletRequest request, HttpServletResponse response){
        pacienteService.concluirCadastro(paciente, user);
        userService.concluirCadastro(user);
        securityService.logout(request, response);
    }
}
