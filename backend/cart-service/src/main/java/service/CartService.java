package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.repository.CartItemRepository;
import com.microservices.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Cart createCart(Cart cart) {
        log.info("Creating cart for userId: {}", cart.getUserId());
        return cartRepository.save(cart);
    }

    public Cart getCartById(Integer id) {
        log.info("Fetching cart by id: {}", id);
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
    }

    public CartItem addItemToCart(CartItem cartItem) {
        log.info("Adding item to cart: {}", cartItem.getCartId());
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItems(Integer cartId) {
        log.info("Fetching items for cartId: {}", cartId);
        return cartItemRepository.findAll()
                .stream()
                .filter(item -> item.getCartId().equals(cartId))
                .toList();
    }
}