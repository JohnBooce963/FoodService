package com.goo.mai_roo.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "food_order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private FoodOrder foodOrder;

    // Name and price are copied at order time, so later menu edits don't rewrite old orders.
    private Long priceId;
    private String foodName;
    private String sizeName;
    private Integer unitPrice;
    private Integer qty;
}
