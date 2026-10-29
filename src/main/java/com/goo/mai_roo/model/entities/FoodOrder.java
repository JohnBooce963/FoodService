package com.goo.mai_roo.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "food_order")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private Integer total;

    private boolean done;

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "foodOrder", cascade = CascadeType.ALL)
    @OrderBy("id")
    @BatchSize(size = 100)
    private List<OrderItem> items = new ArrayList<>();
}
