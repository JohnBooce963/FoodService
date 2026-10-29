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

        return foods.findAll(Sort.by("foodName")).stream()
                .map(f -> new MenuItem(
                        f.getFoodId(),
                        f.getFoodName(),
                        f.getFoodIsPopular() != null && YES.contains(f.getFoodIsPopular().trim().toUpperCase()),
                        f.getFoodImg(),
                        byFood.getOrDefault(f.getFoodId(), List.of()).stream()
                                .map(p -> new MenuItem.Option(p.getFoodPriceId(), sizeName(sizeNames, p), p.getPrice()))
                                .toList()))
                .toList();
    }

    private static String sizeName(Map<Long, String> sizeNames, FoodPrice p) {
        return p.getSizeId() == null ? null : sizeNames.get(p.getSizeId().longValue());
    }
}
