package com.goo.mai_roo.view.rest.controller;

import com.goo.mai_roo.model.service.FoodService;
import com.goo.mai_roo.view.rest.body.MenuItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public List<MenuItem> menu() {
        return foodService.menu();
    }
}
