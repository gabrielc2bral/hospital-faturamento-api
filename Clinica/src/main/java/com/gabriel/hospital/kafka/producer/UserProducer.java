package com.gabriel.hospital.kafka.producer;

import com.gabriel.hospital.auth.Events.UserCreatedEvent;
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
