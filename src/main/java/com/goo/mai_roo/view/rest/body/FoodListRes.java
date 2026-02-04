package com.goo.mai_roo.view.rest.body;

import com.goo.mai_roo.model.entities.FoodList;

import java.util.List;

public class FoodListRes {
    private List<FoodList> dataList;
    private FoodList data;
    private String foodId;

    public FoodListRes(){

    }

    public List<FoodList> getDataList() {
        return dataList;
    }

    public void setDataList(List<FoodList> dataList) {
        this.dataList = dataList;
    }

    public FoodList getData() {
        return data;
    }

    public void setData(FoodList data) {
        this.data = data;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
