package com.goo.mai_roo.view.rest.controller;

import com.goo.mai_roo.model.service.ShopAuth;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public record LoginRequest(String password) {
    }

    private final ShopAuth shopAuth;

    public AuthController(ShopAuth shopAuth) {
        this.shopAuth = shopAuth;
    }

    @PostMapping("/login")
    public ShopAuth.Token login(@RequestBody LoginRequest req) {
        return shopAuth.login(req.password());
    }
}
