package com.example.FF_FastFood.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Foods")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;
    private String name;
    private double price;
    private String description;
    @Setter
    @Getter
    private String category_id;
    private String imageUrl;

    // Getters and setters
}