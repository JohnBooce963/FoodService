package com.goo.mai_roo.view.rest.body;

import java.util.List;

// One food on the menu with every size it is sold in.
public record MenuItem(String id, String name, String category, boolean popular, String img, List<Option> options) {
    public record Option(Long priceId, String size, Integer price) {
    }
}
