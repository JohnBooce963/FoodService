package com.goo.mai_roo.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "food")
public class Food implements Serializable {

    @Id
    @Column(name = "food_id")
    private String foodId;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "food_ispopular")
    private String foodIsPopular;

    @Column(name = "food_img")
    private String foodImg;

}
