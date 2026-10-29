package com.goo.mai_roo.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "food_price")
public class FoodPrice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_price_id")
    private Long foodPriceId;

    @Column(name = "food_price_food_id")
    private String foodId;

    @Column(name = "food_price_size_id")
    private Integer sizeId;

    @Column(name = "food_price_price")
    private Integer price;
}
