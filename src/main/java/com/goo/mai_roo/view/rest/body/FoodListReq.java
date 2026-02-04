package com.goo.mai_roo.view.rest.body;

import lombok.*;

@Getter
@Setter
public class FoodListReq {
    private String foodId;
    private String foodName;
    private Character foodIsPopular;
    private String foodImg;
    private Integer foodMinPrice;
    private Integer foodMaxPrice;
    private String foodSize;
}
