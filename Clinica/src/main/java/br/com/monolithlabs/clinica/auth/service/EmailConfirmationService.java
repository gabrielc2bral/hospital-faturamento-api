package br.com.monolithlabs.clinica.auth.service;

import br.com.monolithlabs.clinica.auth.Events.UserCreatedEvent;
import br.com.monolithlabs.clinica.auth.dto.request.UserEmailConfirmationDTO;
import br.com.monolithlabs.clinica.auth.entity.User;
import br.com.monolithlabs.clinica.auth.entity.UserEmailConfirmation;
import br.com.monolithlabs.clinica.auth.repository.EmailConfirmationRepository;
import br.com.monolithlabs.clinica.kafka.producer.UserProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class EmailConfirmationService {

    private final EmailConfirmationRepository emailConfirmationRepository;
    private final UserProducer userProducer;


    public UserCreatedEvent criarUserEmailConfirmation(User user, UserEmailConfirmation confirmation){
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));

        confirmation.setCode(code);

        confirmation.setEmail(user.getEmail());
        confirmation.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        confirmation.setUsed(false);

        confirmation.setUser(user);

        emailConfirmationRepository.save(confirmation);
        return new UserCreatedEvent(user.getId(), user.getEmail(), code);
    }

    public void enviarEmailConfirmacao(User user){
        UserEmailConfirmation confirmation = new UserEmailConfirmation();


        userProducer.sendUserCreated(criarUserEmailConfirmation(user, confirmation));

    }
    @Transactional
    public void validarCodigoUsuario(UserEmailConfirmationDTO dto){
        UserEmailConfirmation confirmation = emailConfirmationRepository.findByEmailAndUsedFalse(dto.getEmail()).orElseThrow(() -> new RuntimeException());
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
