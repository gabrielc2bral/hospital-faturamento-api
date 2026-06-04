package com.gabriel.hospital.auth.service;

import com.gabriel.hospital.auth.Events.UserCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
    public class usertest {

    @KafkaListener(
            topics = "user-created",
            groupId = "hospital-group-dev"
    )
    public void consume(String mensagem) {

        System.out.println("RECEBIDO -> " + mensagem);

    }


}

