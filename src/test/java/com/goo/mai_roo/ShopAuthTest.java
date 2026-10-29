package com.goo.mai_roo;

import com.goo.mai_roo.model.service.ShopAuth;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class ShopAuthTest {

    @Test
    void rightPasswordGivesATokenThatOpensTheShop() {
        ShopAuth auth = new ShopAuth("correct-horse");
        String token = auth.login("correct-horse").token();

        assertDoesNotThrow(() -> auth.require("Bearer " + token));
        assertStatus(HttpStatus.UNAUTHORIZED, () -> auth.require(null));
        assertStatus(HttpStatus.UNAUTHORIZED, () -> auth.require(token)); // missing "Bearer "
        assertStatus(HttpStatus.UNAUTHORIZED, () -> auth.require("Bearer " + token + "x"));
        assertStatus(HttpStatus.UNAUTHORIZED, () -> auth.require("Bearer 9999999999." + token.split("\\.")[1]));
        // A token from another server (other key) is rejected too.
        assertStatus(HttpStatus.UNAUTHORIZED, () -> new ShopAuth("correct-horse").require("Bearer " + token));
    }

    @Test
    void wrongPasswordsAreRejectedAndThenThrottled() {
        ShopAuth auth = new ShopAuth("correct-horse");
        for (int i = 0; i < 5; i++) assertStatus(HttpStatus.UNAUTHORIZED, () -> auth.login("nope"));
        assertStatus(HttpStatus.TOO_MANY_REQUESTS, () -> auth.login("correct-horse"));
    }

    @Test
    void loginIsOffWithoutAProperPassword() {
        assertStatus(HttpStatus.SERVICE_UNAVAILABLE, () -> new ShopAuth("").login(""));
        assertStatus(HttpStatus.SERVICE_UNAVAILABLE, () -> new ShopAuth("short").login("short"));
    }

    private static void assertStatus(HttpStatus status, org.junit.jupiter.api.function.Executable call) {
        var e = assertThrows(ResponseStatusException.class, call);
        assertEquals(status, e.getStatusCode());
    }
}
