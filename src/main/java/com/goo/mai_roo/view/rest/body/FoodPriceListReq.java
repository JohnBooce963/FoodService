package com.goo.mai_roo.view.rest.body;

import lombok.Data;

@Data
public class FoodPriceListReq {
    private FoodPriceListData data;

    @Data
    public class FoodPriceListData{
        private Long id;
        private String foodId;
        private Integer sizeId;
        private Integer foodMinPrice;
        private Integer foodMaxPrice;
        private String foodName;
        private String sizeName;
    }
}
