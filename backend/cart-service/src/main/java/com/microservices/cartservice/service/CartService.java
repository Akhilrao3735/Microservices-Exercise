package com.microservices.cartservice.service;

import com.microservices.cartservice.dto.CartEventDTO;
import com.microservices.cartservice.dto.ProductDTO;
import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.kafka.CartEventProducer;
import com.microservices.cartservice.repository.CartItemRepository;
import com.microservices.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final WebClient webClient;
    private final CartEventProducer cartEventProducer;

    public Cart createCart(Cart cart) {
        log.info("Creating cart for userId: {}", cart.getUserId());
        return cartRepository.save(cart);
    }

    public Cart getCartById(Integer id) {
        log.info("Fetching cart by id: {}", id);
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
    }

    public CartItem addItemToCart(CartItem cartItem) throws Exception {
        log.info("Async processing - fetching product and validating in parallel");

        CompletableFuture<ProductDTO> productFuture = CompletableFuture.supplyAsync(() ->
                webClient.get()
                        .uri("/api/products/{id}", cartItem.getProductId())
                        .retrieve()
                        .bodyToMono(ProductDTO.class)
                        .block()
        );

        CompletableFuture<Void> logFuture = CompletableFuture.runAsync(() ->
                log.info("Started stock validation for productId: {}", cartItem.getProductId())
        );

        CompletableFuture.allOf(productFuture, logFuture).join();

        ProductDTO product = productFuture.get();

        if (product == null) {
            throw new RuntimeException("Product not found with id: " + cartItem.getProductId());
        }

        if (product.getStock() < cartItem.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
        }

        log.info("Async tasks completed. Adding item to cart: {}", cartItem.getCartId());
        CartItem savedItem = cartItemRepository.save(cartItem);

        CartEventDTO event = new CartEventDTO(
                savedItem.getCartId(),
                savedItem.getProductId(),
                savedItem.getQuantity()
        );
        cartEventProducer.sendCartEvent(event);

        return savedItem;
    }

    public List<CartItem> getCartItems(Integer cartId) {
        log.info("Fetching items for cartId: {}", cartId);
        return cartItemRepository.findAll()
                .stream()
                .filter(item -> item.getCartId().equals(cartId))
                .toList();
    }
}