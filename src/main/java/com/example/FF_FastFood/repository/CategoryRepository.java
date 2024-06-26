package com.example.FF_FastFood.repository;

import com.example.FF_FastFood.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<FoodCategory, Long> {
}
