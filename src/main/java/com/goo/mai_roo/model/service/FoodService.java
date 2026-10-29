package com.goo.mai_roo.model.service;

import com.goo.mai_roo.model.entities.FoodPrice;
import com.goo.mai_roo.model.entities.FoodSize;
import com.goo.mai_roo.model.repository.FoodPriceRepository;
import com.goo.mai_roo.model.repository.FoodRepository;
import com.goo.mai_roo.model.repository.FoodSizeRepository;
import com.goo.mai_roo.view.rest.body.MenuItem;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private static final Set<String> YES = Set.of("Y", "YES", "1", "T", "TRUE");

    // ponytail: the category is the first two digits of food_id (the shop's own numbering).
    // Move it to a food_category table once the shop wants to rename or reorder categories itself.
    private static final Map<String, String> CATEGORIES = Map.ofEntries(
            Map.entry("10", "ของทอด"),
            Map.entry("20", "แกง"), Map.entry("21", "แกง"),
            Map.entry("30", "ต้มยำ"),
            Map.entry("40", "ข้าว"),
            Map.entry("41", "ผัด"), Map.entry("42", "ผัด"), Map.entry("43", "ผัด"), Map.entry("44", "ผัด"),
            Map.entry("50", "น้ำพริก"),
            Map.entry("60", "ยำ & ลวกจิ้ม"),
            Map.entry("70", "ลาบ & น้ำตก"),
            Map.entry("80", "เครื่องดื่ม"));
    static final String OTHER = "อื่นๆ";

    private final FoodRepository foods;
    private final FoodPriceRepository prices;
    private final FoodSizeRepository sizes;

    public FoodService(FoodRepository foods, FoodPriceRepository prices, FoodSizeRepository sizes) {
        this.foods = foods;
        this.prices = prices;
        this.sizes = sizes;
    }

    // ponytail: reads the three menu tables on every request; fine for one restaurant, cache it if the menu gets big.
    public List<MenuItem> menu() {
        Map<Long, String> sizeNames = new HashMap<>();
        for (FoodSize s : sizes.findAll()) sizeNames.put(s.getId(), s.getSizeName());

        Map<String, List<FoodPrice>> byFood = prices.findAll(Sort.by("price")).stream()
                .filter(p -> p.getFoodId() != null && p.getPrice() != null)
                .collect(Collectors.groupingBy(FoodPrice::getFoodId));

        // Sorted by id, so categories and dishes come out in the shop's own order.
        return foods.findAll(Sort.by("foodId")).stream()
                .filter(f -> f.getFoodName() != null && !f.getFoodName().isBlank())
                .map(f -> new MenuItem(
                        f.getFoodId(),
                        f.getFoodName(),
                        category(f.getFoodId()),
                        f.getFoodIsPopular() != null && YES.contains(f.getFoodIsPopular().trim().toUpperCase()),
                        f.getFoodImg(),
                        byFood.getOrDefault(f.getFoodId(), List.of()).stream()
                                .map(p -> new MenuItem.Option(p.getFoodPriceId(), sizeName(sizeNames, p), p.getPrice()))
                                .toList()))
                .toList();
    }

    static String category(String foodId) {
        return foodId == null || foodId.length() < 2 ? OTHER : CATEGORIES.getOrDefault(foodId.substring(0, 2), OTHER);
    }

    private static String sizeName(Map<Long, String> sizeNames, FoodPrice p) {
        return p.getSizeId() == null ? null : sizeNames.get(p.getSizeId().longValue());
    }
}
