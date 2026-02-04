package com.goo.mai_roo.view.rest.body;

import lombok.Data;

@Data
public class FoodPriceReq {
    private FoodPriceData data;

    @Data
    public class FoodPriceData{
        private Long id;
        private String foodId;
        private Integer sizeId;
        private Integer foodPrice;
    }
}
