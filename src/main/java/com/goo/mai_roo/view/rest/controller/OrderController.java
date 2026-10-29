package com.goo.mai_roo.view.rest.controller;

import com.goo.mai_roo.model.entities.FoodOrder;
import com.goo.mai_roo.model.service.OrderService;
import com.goo.mai_roo.model.service.ShopAuth;
import com.goo.mai_roo.view.rest.body.OrderRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ShopAuth shopAuth;

    public OrderController(OrderService orderService, ShopAuth shopAuth) {
        this.orderService = orderService;
        this.shopAuth = shopAuth;
    }

    // Customers place orders without logging in; reading and closing them is shop only.
    @PostMapping
    public FoodOrder place(@RequestBody OrderRequest req) {
        return orderService.place(req);
    }

    @GetMapping
    public List<FoodOrder> latest(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth) {
        shopAuth.require(auth);
        return orderService.latest();
    }

    @PostMapping("/{id}/done")
    public FoodOrder done(@PathVariable Long id,
                          @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth) {
        shopAuth.require(auth);
        return orderService.markDone(id);
    }
}
