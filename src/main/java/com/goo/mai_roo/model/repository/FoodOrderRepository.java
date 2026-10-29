package com.goo.mai_roo.model.repository;

import com.goo.mai_roo.model.entities.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findTop100ByOrderByIdDesc();
}
