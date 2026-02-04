package com.goo.mai_roo.model.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodPriceList {
    private Long priceId;
    private String foodId;
    private Integer sizeId;
    private Integer foodPrice;
    private String foodName;
    private String sizeName;

    public FoodPriceList(Long priceId, String foodId, Integer sizeId, Integer foodPrice, String foodName, String sizeName) {
        this.priceId = priceId;
        this.foodId = foodId;
        this.sizeId = sizeId;
        this.foodPrice = foodPrice;
        this.foodName = foodName;
        this.sizeName = sizeName;
    }
}
