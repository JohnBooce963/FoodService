package com.goo.mai_roo.model.repository;

import com.goo.mai_roo.model.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, String> {
}
