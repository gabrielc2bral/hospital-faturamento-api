package br.com.monolithlabs.clinica.auth.service;


import br.com.monolithlabs.clinica.auth.dto.request.CadastroDtoRequest;
import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.auth.enums.UserRole;
import br.com.monolithlabs.clinica.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmationService emailConfirmationService;

    public void cadastrarUsuario(CadastroDtoRequest dto){
        if (userRepository.existsByEmail(dto.getEmail())) throw new RuntimeException();
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setUsuarioAtivo(false);
        user.setRole(UserRole.PENDING);

        userRepository.save(user);
        emailConfirmationService.enviarEmailConfirmacao(user);
    }
    public void concluirCadastro(User userLogado) {
        User user = userRepository.findByEmail(userLogado.getEmail()).orElseThrow();
        user.setRole(UserRole.PACIENTE);
        userRepository.save(user);
    }
}
