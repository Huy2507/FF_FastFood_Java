package com.example.FF_FastFood.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bannerIMG;
    private LocalDate startDate;
    private LocalDate endDate;

    // Getters and setters
}
