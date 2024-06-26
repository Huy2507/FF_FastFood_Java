package com.example.FF_FastFood.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "Cart_Items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @Setter
    @Getter
    private Long food_id;
    private int quantity;
    private Date createdAt;
    private Date updatedAt;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "food_id", insertable = false, updatable = false)
    private FoodItem food;

}
