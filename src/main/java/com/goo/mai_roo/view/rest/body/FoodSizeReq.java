package com.goo.mai_roo.view.rest.body;

import lombok.Data;

@Data
public class FoodSizeReq {
    private FoodSizeData data;

    @Data
    public class FoodSizeData{
        private Long id;
        private String sizeName;
    }
}
