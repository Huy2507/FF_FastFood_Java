package com.example.FF_FastFood.service;

import com.example.FF_FastFood.entity.Cart;
import com.example.FF_FastFood.entity.CartItem;
import com.example.FF_FastFood.entity.FoodItem;

public interface CartService {
    void addToCart(Long foodId, Long customerId);
    void updateQuantity(Long cartItemId, String changeQuantity);
    void removeItem(Long cartItemId);
    Cart getCartByCustomerId(Long customerId);
    FoodItem getFoodItemById(Long foodId);
    void removeCartItem(CartItem cartItem);
}
