package com.gabriel.hospital.auth.service;

import com.gabriel.hospital.auth.dto.request.UserEmailConfirmationDTO;
import com.gabriel.hospital.auth.entity.User;
import com.gabriel.hospital.auth.entity.UserEmailConfirmation;
import com.gabriel.hospital.auth.repository.EmailConfirmationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class EmailConfirmationService {

    private final EmailConfirmationRepository emailConfirmationRepository;
    private final JavaMailSender mailSender;


    public void criarUserEmailConfirmation(User user, UserEmailConfirmation confirmation){
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));

        confirmation.setCode(code);

        confirmation.setEmail(user.getEmail());
        confirmation.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        confirmation.setUsed(false);

        confirmation.setUser(user);

        emailConfirmationRepository.save(confirmation);
    }

    public void enviarEmailConfirmacao(User user){
        UserEmailConfirmation confirmation = new UserEmailConfirmation();

        criarUserEmailConfirmation(user, confirmation);

        SimpleMailMessage message = new SimpleMailMessage();


        message.setFrom("noreply@demomailtrap.co");
        message.setTo(user.getEmail());

        message.setSubject("Confirmação de cadastro");

        message.setText("Seu código é: " + confirmation.getCode());

        mailSender.send(message);
    }
    @Transactional
    public void validarCodigoUsuario(UserEmailConfirmationDTO dto){
        UserEmailConfirmation confirmation = emailConfirmationRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException());
        if (LocalDateTime.now().isAfter(confirmation.getExpiresAt())) throw new RuntimeException();
        User user = confirmation.getUser();
        if (confirmation.isUsed() || user.isUsuarioAtivo()) {
            throw new RuntimeException();
        }
        if (!confirmation.getCode().equals(dto.getCode())){
            throw new RuntimeException();
        }
        confirmation.setUsed(true);
        user.setUsuarioAtivo(true);
    }
}
