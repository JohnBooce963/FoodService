package com.goo.mai_roo.view.rest.body;

import com.goo.mai_roo.model.entities.FoodPrice;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodPriceRes {
    private FoodPrice data;
    private String foodId;
}
