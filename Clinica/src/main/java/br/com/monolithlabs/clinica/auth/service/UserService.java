package br.com.monolithlabs.clinica.auth.service;


import br.com.monolithlabs.clinica.auth.dto.request.CadastroDtoRequest;
import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.auth.enums.UserRole;
import br.com.monolithlabs.clinica.auth.repository.UserRepository;
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

}
