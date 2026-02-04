package com.goo.mai_roo.view.rest.body;

import com.goo.mai_roo.model.entities.FoodList;
import com.goo.mai_roo.model.entities.FoodPriceList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodPriceListRes {
    private List<FoodPriceList> dataList;
    private FoodPriceList data;
    private String foodId;
}
