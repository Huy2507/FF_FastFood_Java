package com.example.FF_FastFood.service;

import com.example.FF_FastFood.entity.Cart;
import com.example.FF_FastFood.entity.CartItem;
import com.example.FF_FastFood.entity.FoodItem;
import com.example.FF_FastFood.repository.CartItemRepository;
import com.example.FF_FastFood.repository.CartRepository;
import com.example.FF_FastFood.repository.FoodRepository;
import com.example.FF_FastFood.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public void addToCart(Long foodId, Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);

        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
            cart.setCreatedAt(Date.valueOf(String.valueOf(System.currentTimeMillis())));
            cart.setUpdatedAt(Date.valueOf(String.valueOf(System.currentTimeMillis())));
            cartRepository.save(cart);
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getFood_id().equals(foodId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setUpdatedAt(Date.valueOf(String.valueOf(System.currentTimeMillis())));
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setFood_id(foodId);
            newItem.setQuantity(1);
            newItem.setCreatedAt(Date.valueOf(String.valueOf(System.currentTimeMillis())));
            newItem.setUpdatedAt(Date.valueOf(String.valueOf(System.currentTimeMillis())));
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        cart.setUpdatedAt(Date.valueOf(String.valueOf(System.currentTimeMillis())));
        cartRepository.save(cart);
    }

    @Override
    public void updateQuantity(Long cartItemId, String changeQuantity) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();

            if (changeQuantity.equals("+")) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            } else if (changeQuantity.equals("-") && cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
            }

            cartItem.setUpdatedAt(Date.valueOf(String.valueOf(System.currentTimeMillis())));
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public void removeItem(Long cartItemId) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.getCart().getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }
    }

    @Override
    public Cart getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    @Override
    public FoodItem getFoodItemById(Long foodId) {
        return foodRepository.findById(foodId).orElse(null);
    }

    @Override
    public void removeCartItem(CartItem cartItem) {
        cartItem.getCart().getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }
}
