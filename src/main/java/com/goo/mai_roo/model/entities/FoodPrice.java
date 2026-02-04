package com.goo.mai_roo.model.entities;

import jakarta.persistence.*;

import java.io.Serializable;

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

    public Long getFoodPriceId() {
        return foodPriceId;
    }

    public void setFoodPriceId(Long foodPriceId) {
        this.foodPriceId = foodPriceId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public Integer getSizeId() {
        return sizeId;
    }

    public void setSizeId(Integer sizeId) {
        this.sizeId = sizeId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
