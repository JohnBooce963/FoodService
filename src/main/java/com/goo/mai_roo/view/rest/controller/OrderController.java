package com.goo.mai_roo.view.rest.controller;

import com.goo.mai_roo.model.entities.FoodOrder;
import com.goo.mai_roo.model.service.OrderService;
import com.goo.mai_roo.view.rest.body.OrderRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public FoodOrder place(@RequestBody OrderRequest req) {
        return orderService.place(req);
    }

    // ponytail: no shop login yet, so anyone with the URL can see and close orders. Add auth before real use.
    @GetMapping
    public List<FoodOrder> latest() {
        return orderService.latest();
    }

    @PostMapping("/{id}/done")
    public FoodOrder done(@PathVariable Long id) {
        return orderService.markDone(id);
    }
}
