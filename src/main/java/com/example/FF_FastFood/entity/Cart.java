package com.example.FF_FastFood.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    private Long customerId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private Date createdAt;
    private Date updatedAt;

    // Constructors, getters, and setters
    public Cart() {
    }

    public Cart(Long customerId) {
        this.customerId = customerId;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }


    public double getTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getQuantity() * item.getFood().getPrice();
        }
        return totalPrice;
    }
}
