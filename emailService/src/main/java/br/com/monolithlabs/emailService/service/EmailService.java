package br.com.monolithlabs.emailService.service;

import br.com.monolithlabs.emailService.Events.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @KafkaListener(
            topics = "user-created",
            groupId = "hospital-group"
    )
    public void sendEmail(UserCreatedEvent event){
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(event.getEmail());
        mensagem.setFrom("noreply@demomailtrap.co");
        mensagem.setSubject("Confirmação de cadastro");
        mensagem.setText(
                """
                Olá,

                Seu código de confirmação é:

                %s

                Este código expira em 10 minutos.
                """
                        .formatted(event.getCode())
        );

        mailSender.send(mensagem);
    }
}
