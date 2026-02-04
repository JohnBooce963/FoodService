package com.goo.mai_roo.view.rest.body;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodReq {
    private String foodId;
    private String foodName;
    private String foodIsPopular;
    private String foodImg;
    private Integer foodPrice;
    private Long foodPriceId;
    private Integer foodSizeId;
}
