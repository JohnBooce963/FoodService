package com.goo.mai_roo.model.service;

import com.goo.mai_roo.model.entities.Food;
import com.goo.mai_roo.model.entities.FoodOrder;
import com.goo.mai_roo.model.entities.FoodPrice;
import com.goo.mai_roo.model.entities.FoodSize;
import com.goo.mai_roo.model.entities.OrderItem;
import com.goo.mai_roo.model.repository.FoodOrderRepository;
import com.goo.mai_roo.model.repository.FoodPriceRepository;
import com.goo.mai_roo.model.repository.FoodRepository;
import com.goo.mai_roo.model.repository.FoodSizeRepository;
import com.goo.mai_roo.view.rest.body.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {

    static final int MAX_LINES = 50;
    static final int MAX_QTY = 99;

    private final FoodOrderRepository orders;
    private final FoodRepository foods;
    private final FoodPriceRepository prices;
    private final FoodSizeRepository sizes;

    public OrderService(FoodOrderRepository orders, FoodRepository foods, FoodPriceRepository prices, FoodSizeRepository sizes) {
        this.orders = orders;
        this.foods = foods;
        this.prices = prices;
        this.sizes = sizes;
    }

    @Transactional
    public FoodOrder place(OrderRequest req) {
        String name = req.customerName() == null ? "" : req.customerName().strip();
        if (name.isEmpty() || name.length() > 50) throw bad("customerName must be 1-50 characters");
        if (req.items() == null || req.items().isEmpty() || req.items().size() > MAX_LINES) {
            throw bad("An order needs 1-" + MAX_LINES + " items");
        }

        FoodOrder order = new FoodOrder();
        order.setCustomerName(name);
        int total = 0;
        for (OrderRequest.Line line : req.items()) {
            if (line == null || line.priceId() == null) throw bad("Every item needs a priceId");
            if (line.qty() == null || line.qty() < 1 || line.qty() > MAX_QTY) throw bad("qty must be 1-" + MAX_QTY);

            FoodPrice price = prices.findById(line.priceId())
                    .filter(p -> p.getPrice() != null && p.getFoodId() != null)
                    .orElseThrow(() -> bad("Unknown priceId " + line.priceId()));
            Food food = foods.findById(price.getFoodId()).orElseThrow(() -> bad("Unknown priceId " + line.priceId()));

            OrderItem item = new OrderItem();
            item.setFoodOrder(order);
            item.setPriceId(price.getFoodPriceId());
            item.setFoodName(food.getFoodName());
            item.setSizeName(price.getSizeId() == null ? null
                    : sizes.findById(price.getSizeId().longValue()).map(FoodSize::getSizeName).orElse(null));
            item.setUnitPrice(price.getPrice());
            item.setQty(line.qty());
            order.getItems().add(item);
            total += price.getPrice() * line.qty();
        }
        order.setTotal(total);
        return orders.save(order);
    }

    public List<FoodOrder> latest() {
        return orders.findTop100ByOrderByIdDesc();
    }

    @Transactional
    public FoodOrder markDone(Long id) {
        FoodOrder order = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        order.setDone(true);
        return order;
    }

    private static ResponseStatusException bad(String reason) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
    }
}
