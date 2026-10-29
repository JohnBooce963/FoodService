package com.goo.mai_roo.view.rest.body;

import java.util.List;

// The client only says what and how many; prices are looked up on the server.
public record OrderRequest(String customerName, List<Line> items) {
    public record Line(Long priceId, Integer qty) {
    }
}
