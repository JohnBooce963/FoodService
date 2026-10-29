package com.goo.mai_roo;

import com.goo.mai_roo.model.entities.Food;
import com.goo.mai_roo.model.entities.FoodOrder;
import com.goo.mai_roo.model.entities.FoodPrice;
import com.goo.mai_roo.model.entities.FoodSize;
import com.goo.mai_roo.model.entities.OrderItem;
import com.goo.mai_roo.model.repository.FoodPriceRepository;
import com.goo.mai_roo.model.repository.FoodRepository;
import com.goo.mai_roo.model.repository.FoodSizeRepository;
import com.goo.mai_roo.model.service.FoodService;
import com.goo.mai_roo.model.service.OrderService;
import com.goo.mai_roo.view.rest.body.MenuItem;
import com.goo.mai_roo.view.rest.body.OrderRequest;
import com.goo.mai_roo.view.rest.body.OrderRequest.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Runs against in-memory H2, so no real database is needed. The main application.properties still loads.
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE",
        "spring.jpa.hibernate.ddl-auto=create-drop",
})
@Transactional
class OrderServiceTest {

    @Autowired OrderService orders;
    @Autowired FoodService menu;
    @Autowired FoodRepository foods;
    @Autowired FoodSizeRepository sizes;
    @Autowired FoodPriceRepository prices;

    Long priceId;

    @BeforeEach
    void seed() {
        Food food = new Food();
        food.setFoodId("F1");
        food.setFoodName("Pad Thai");
        food.setFoodIsPopular("Y");
        foods.save(food);

        FoodSize size = new FoodSize();
        size.setSizeName("Large");
        sizes.save(size);

        FoodPrice price = new FoodPrice();
        price.setFoodId("F1");
        price.setSizeId(size.getId().intValue());
        price.setPrice(60);
        priceId = prices.save(price).getFoodPriceId();
    }

    @Test
    void totalComesFromDbPricesAndItemsKeepASnapshot() {
        FoodOrder o = orders.place(new OrderRequest("  Ann ", List.of(new Line(priceId, 2))));

        assertEquals(120, o.getTotal());
        assertEquals("Ann", o.getCustomerName());
        assertFalse(o.isDone());
        OrderItem item = o.getItems().getFirst();
        assertEquals("Pad Thai", item.getFoodName());
        assertEquals("Large", item.getSizeName());
        assertEquals(60, item.getUnitPrice());
        assertEquals(2, item.getQty());

        assertTrue(orders.markDone(o.getId()).isDone());
    }

    @Test
    void badOrdersAreRejected() {
        assertBad(new OrderRequest("Ann", List.of()));
        assertBad(new OrderRequest(" ", List.of(new Line(priceId, 1))));
        assertBad(new OrderRequest("Ann", List.of(new Line(priceId, 0))));
        assertBad(new OrderRequest("Ann", List.of(new Line(priceId, 100))));
        assertBad(new OrderRequest("Ann", List.of(new Line(999L, 1))));
        assertBad(new OrderRequest("Ann", List.of(new Line(null, 1))));
    }

    @Test
    void menuListsEachSizeUnderItsFood() {
        MenuItem item = menu.menu().getFirst();

        assertEquals("Pad Thai", item.name());
        assertTrue(item.popular());
        assertEquals(List.of(new MenuItem.Option(priceId, "Large", 60)), item.options());
    }

    private void assertBad(OrderRequest req) {
        var e = assertThrows(ResponseStatusException.class, () -> orders.place(req));
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
}
