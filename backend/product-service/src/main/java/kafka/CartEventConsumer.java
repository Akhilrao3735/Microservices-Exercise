package com.microservices.productservice.kafka;

import com.microservices.productservice.dto.CartEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartEventConsumer {

    @KafkaListener(topics = "cart-events", groupId = "product-service-group")
    public void consumeCartEvent(CartEventDTO event) {
        log.info("Received cart event from Kafka: cartId={}, productId={}, quantity={}",
                event.getCartId(), event.getProductId(), event.getQuantity());
    }
}