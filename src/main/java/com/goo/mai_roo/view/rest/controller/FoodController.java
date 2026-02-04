package com.goo.mai_roo.view.rest.controller;

import com.goo.mai_roo.model.entities.FoodPrice;
import com.goo.mai_roo.view.rest.body.FoodListReq;
import com.goo.mai_roo.view.rest.body.FoodListRes;
import com.goo.mai_roo.view.rest.body.FoodReq;
import com.goo.mai_roo.model.entities.Food;
import com.goo.mai_roo.model.entities.FoodList;
import com.goo.mai_roo.model.service.FoodService;
import com.goo.mai_roo.view.rest.body.FoodRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping("/getFoodList")
    public FoodListRes getFoodList(@RequestBody FoodListReq requestBody) throws Exception{
        FoodListRes response = new FoodListRes();
        try {
            List<FoodList> resultList = foodService.findFoodListbySQL(requestBody);
            response.setDataList(resultList);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return response;
    }

//    @PostMapping("/testFindById")
//    public FoodRes findbyId(@RequestBody FoodReq req) throws Exception{
//        FoodRes res = new FoodRes();
//        try{
//            Food target = foodService.findById(req.getFoodId());
//            res.setData(target);
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//
//        return res;
//    }

    @PostMapping("/findFood")
    public FoodRes findFood(@RequestBody FoodReq requestBody) throws Exception {
        FoodRes response = new FoodRes();
        try {
            Food result = foodService.findById(requestBody.getFoodId());
            response.setData(result);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return response;
    }

    @PostMapping("/updateFood")
    public FoodRes updateFoodById(@RequestBody FoodReq requestBody) throws Exception{
        FoodRes response = new FoodRes();
        try{
            Food target = foodService.findById(requestBody.getFoodId());
            FoodPrice priceTarget = foodService.findPriceById(requestBody.getFoodPriceId());

            target.setFoodName(requestBody.getFoodName());
            target.setFoodIsPopular(requestBody.getFoodIsPopular());
            target.setFoodImg(requestBody.getFoodImg());

            priceTarget.setPrice(requestBody.getFoodPrice());
            priceTarget.setSizeId(requestBody.getFoodSizeId());

            foodService.update(target);
            foodService.updatePrice(priceTarget);

            List<Object> resultList = new ArrayList<Object>();
            resultList.add(target);
            resultList.add(priceTarget);

            response.setDataList(resultList);
            response.setData(target);
            response.setFoodId(target.getFoodId());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return response;
    }
}
