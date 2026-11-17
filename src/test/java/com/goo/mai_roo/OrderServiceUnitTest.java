package com.goo.mai_roo;

import com.goo.mai_roo.model.entities.Food;
import com.goo.mai_roo.model.entities.FoodOrder;
import com.goo.mai_roo.model.entities.FoodPrice;
import com.goo.mai_roo.model.entities.FoodSize;
import com.goo.mai_roo.model.entities.OrderItem;
import com.goo.mai_roo.model.repository.FoodOrderRepository;
import com.goo.mai_roo.model.repository.FoodPriceRepository;
import com.goo.mai_roo.model.repository.FoodRepository;
import com.goo.mai_roo.model.repository.FoodSizeRepository;
import com.goo.mai_roo.model.service.OrderService;
import com.goo.mai_roo.view.rest.body.OrderRequest;
import com.goo.mai_roo.view.rest.body.OrderRequest.Line;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Unit test: repositories are Mockito mocks, so no Spring context and no database.
// OrderServiceTest covers the same service end to end against H2.
@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock FoodOrderRepository orders;
    @Mock FoodRepository foods;
    @Mock FoodPriceRepository prices;
    @Mock FoodSizeRepository sizes;
    @InjectMocks OrderService service;

    @Test
    void totalIsDbPriceTimesQtyAndItemsKeepASnapshot() {
        when(prices.findById(1L)).thenReturn(Optional.of(price(1L, "10001", 2, 60)));
        when(prices.findById(2L)).thenReturn(Optional.of(price(2L, "10002", null, 45)));
        when(foods.findById("10001")).thenReturn(Optional.of(food("10001", "Pad Thai")));
        when(foods.findById("10002")).thenReturn(Optional.of(food("10002", "Som Tam")));
        when(sizes.findById(2L)).thenReturn(Optional.of(size("Large")));
        when(orders.save(any())).thenAnswer(call -> call.getArgument(0));

        FoodOrder order = service.place(new OrderRequest(" Ann ", List.of(new Line(1L, 2), new Line(2L, 1))));

        assertEquals(165, order.getTotal()); // 60 x 2 + 45 x 1
        assertEquals("Ann", order.getCustomerName());
        OrderItem first = order.getItems().get(0);
        assertEquals("Pad Thai", first.getFoodName());
        assertEquals("Large", first.getSizeName());
        assertEquals(60, first.getUnitPrice());
        assertEquals(2, first.getQty());
        assertNull(order.getItems().get(1).getSizeName()); // a price with no size
        verify(orders).save(order);
    }

    @Test
    void unknownPriceIsRejectedAndNothingIsSaved() {
        when(prices.findById(999L)).thenReturn(Optional.empty());

        assertStatus(HttpStatus.BAD_REQUEST, () -> service.place(new OrderRequest("Ann", List.of(new Line(999L, 1)))));
        verify(orders, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 100})
    void qtyOutside1To99IsRejectedBeforeAnyLookup(int qty) {
        assertStatus(HttpStatus.BAD_REQUEST, () -> service.place(new OrderRequest("Ann", List.of(new Line(1L, qty)))));
        verifyNoInteractions(prices, foods, sizes, orders);
    }

    @Test
    void blankNameIsRejectedBeforeAnyLookup() {
        assertStatus(HttpStatus.BAD_REQUEST, () -> service.place(new OrderRequest("   ", List.of(new Line(1L, 1)))));
        verifyNoInteractions(prices, foods, sizes, orders);
    }

    @Test
    void markingAMissingOrderDoneGives404() {
        when(orders.findById(42L)).thenReturn(Optional.empty());

        assertStatus(HttpStatus.NOT_FOUND, () -> service.markDone(42L));
    }

    private static FoodPrice price(Long id, String foodId, Integer sizeId, int amount) {
        FoodPrice p = new FoodPrice();
        p.setFoodPriceId(id);
        p.setFoodId(foodId);
        p.setSizeId(sizeId);
        p.setPrice(amount);
        return p;
    }

    private static Food food(String id, String name) {
        Food f = new Food();
        f.setFoodId(id);
        f.setFoodName(name);
        return f;
    }

    private static FoodSize size(String name) {
        FoodSize s = new FoodSize();
        s.setSizeName(name);
        return s;
    }

    private static void assertStatus(HttpStatus status, org.junit.jupiter.api.function.Executable call) {
        var e = assertThrows(ResponseStatusException.class, call);
        assertEquals(status, e.getStatusCode());
    }
}
