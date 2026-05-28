package com.gabriel.hospital.auth.service;

import com.gabriel.hospital.auth.entity.User;
import com.gabriel.hospital.auth.entity.UserEmailConfirmation;
import com.gabriel.hospital.auth.repository.EmailConfirmationRepository;
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


    public UserEmailConfirmation getEmailConfirmation(String email) {
        UserEmailConfirmation confirmation = emailConfirmationRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
        return confirmation;
    }
    public void changeUserEmailConfirmationValidation(String email){
        emailConfirmationRepository.save(getEmailConfirmation(email));
    }

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
}
