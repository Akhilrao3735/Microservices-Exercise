package com.microservices.cartservice.kafka;

import com.microservices.cartservice.dto.CartEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartEventProducer {

    private static final String TOPIC = "cart-events";

    private final KafkaTemplate<String, CartEventDTO> kafkaTemplate;

    public void sendCartEvent(CartEventDTO event) {
        log.info("Publishing cart event to Kafka topic '{}': {}", TOPIC, event);
        kafkaTemplate.send(TOPIC, event);
    }
}