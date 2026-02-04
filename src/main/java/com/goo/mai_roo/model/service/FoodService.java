package com.goo.mai_roo.model.service;

import com.goo.mai_roo.model.entities.*;
import com.goo.mai_roo.model.dao.FoodDAO;
import com.goo.mai_roo.view.rest.body.FoodListReq;
import com.goo.mai_roo.view.rest.body.FoodPriceListReq;
import com.goo.mai_roo.view.rest.body.FoodReq;
import com.goo.mai_roo.view.rest.body.FoodSizeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodDAO dao;

    public void insert(Food food){
        dao.insert(food);
    }
    public void insertPrice(FoodPrice foodPrice) { dao.insertPrice(foodPrice); }
    public void insertSize(FoodSize foodSize) { dao.insertSize(foodSize); }

    public void update(Food food) { dao.update(food); }
    public void updatePrice(FoodPrice foodPrice) { dao.updatePrice(foodPrice); }
    public void updateSize(FoodSize foodSize) { dao.updateSize(foodSize); }

    public void delete(String id) { dao.delete(id); }
    public void deletePrice(Long priceId) { dao.deletePrice(priceId); }
    public void deleteSize(Long sizeId) { dao.deleteSize(sizeId); }

    public Food findById(String id) { return dao.findById(id); }
    public FoodPrice findPriceById(Long id) { return dao.findPriceById(id); }
    public FoodSize findSizeById(Long id) { return dao.findSizeById(id); }

    public List<FoodList> getFoodListBySql() throws Exception { return dao.getFoodListBySql(); }
    public List<FoodPriceList> getFoodPriceListBySql() throws Exception { return dao.getFoodPriceListBySql(); }
    public List<FoodSize> getFoodSizeListBySql() throws Exception { return dao.getFoodSizeListBySql(); }

    public List<FoodList> findFoodListbySQL(FoodListReq req) throws Exception { return dao.findFoodListbySQL(req); }
    public List<FoodPriceList> findFoodPriceListbySQL(FoodPriceListReq req) throws Exception { return dao.findFoodPriceListbySQL(req); }
    public FoodSize findFoodSizebySQL(FoodSizeReq req) throws Exception { return dao.findFoodSizebySQL(req); }

}
