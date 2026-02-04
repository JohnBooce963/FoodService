package com.goo.mai_roo.view.rest.body;

import com.goo.mai_roo.model.entities.Food;

import java.util.List;

public class FoodRes {
    private List<Object> dataList;
    private Food data;
    private String foodId;

    public Food getData() {
        return data;
    }

    public void setData(Food data) {
        this.data = data;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public List<Object> getDataList() {
        return dataList;
    }

    public void setDataList(List<Object> dataList) {
        this.dataList = dataList;
    }
}
