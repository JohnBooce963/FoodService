package com.goo.mai_roo.model.entities;

public class FoodList {

    public FoodList(String foodId, String foodName, Character foodIsPopular, String foodImg, Integer foodPrice, Integer foodSizeId, String foodSizeName) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodIsPopular = foodIsPopular;
        this.foodImg = foodImg;
        this.foodPrice = foodPrice;
        this.foodSizeId = foodSizeId;
        this.foodSizeName = foodSizeName;
    }

    private String foodId;
    private String foodName;
    private Character foodIsPopular;
    private String foodImg;
    private Integer foodPrice;
    private Integer foodSizeId;
    private String foodSizeName;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Character getFoodIsPopular() {
        return foodIsPopular;
    }

    public void setFoodIsPopular(Character foodIsPopular) {
        this.foodIsPopular = foodIsPopular;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public Integer getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Integer foodPrice) {
        this.foodPrice = foodPrice;
    }

    public Integer getFoodSizeId() {
        return foodSizeId;
    }

    public void setFoodSizeId(Integer foodSizeId) {
        this.foodSizeId = foodSizeId;
    }

    public String getFoodSizeName() {
        return foodSizeName;
    }

    public void setFoodSizeName(String foodSizeName) {
        this.foodSizeName = foodSizeName;
    }
}
