package com.gabriel.hospital.auth.service;

import com.gabriel.hospital.auth.dto.request.UserDtoRequest;
import com.gabriel.hospital.auth.entity.User;
import com.gabriel.hospital.auth.enums.UserRole;
import com.gabriel.hospital.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmationService emailConfirmationService;

    public void cadastrarUsuario(UserDtoRequest dto){

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setUsuarioAtivo(false);
        user.setRole(UserRole.PENDING);

        userRepository.save(user);
        emailConfirmationService.enviarEmailConfirmacao(user);
    }

}
