package br.com.monolithlabs.clinica.kafka.producer;


import br.com.monolithlabs.clinica.auth.Events.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProducer {
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void sendUserCreated(UserCreatedEvent event) {

        kafkaTemplate.send(
                "user-created",
                event
        );

    }
}
