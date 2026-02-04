package com.goo.mai_roo.model.dao;

import com.goo.mai_roo.model.entities.*;
import com.goo.mai_roo.view.rest.body.FoodListReq;
import com.goo.mai_roo.view.rest.body.FoodPriceListReq;
import com.goo.mai_roo.view.rest.body.FoodReq;
import com.goo.mai_roo.view.rest.body.FoodSizeReq;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FoodDAO {
    //insert
    void insert(Food food);
    void insertPrice(FoodPrice foodPrice);
    void insertSize(FoodSize foodSize);

    //update
    void update(Food food);
    void updatePrice(FoodPrice foodPrice);
    void updateSize(FoodSize foodSize);

    //delete
    void delete(String id);
    void deletePrice(Long priceId);
    void deleteSize(Long sizeId);

    //find
    Food findById(String id);
    FoodPrice findPriceById(Long id);
    FoodSize findSizeById(Long id);

    //List
    List<FoodList> getFoodListBySql() throws Exception;
    List<FoodPriceList> getFoodPriceListBySql() throws Exception;
    List<FoodSize> getFoodSizeListBySql() throws Exception;

    //Find
    List<FoodList> findFoodListbySQL(FoodListReq req) throws Exception;
    List<FoodPriceList> findFoodPriceListbySQL(FoodPriceListReq req) throws Exception;
    FoodSize findFoodSizebySQL(FoodSizeReq req) throws Exception;

}
