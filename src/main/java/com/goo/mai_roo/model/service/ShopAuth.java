package com.goo.mai_roo.model.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Base64;
import java.util.Deque;

// One shared shop password (SHOP_PASSWORD) -> a signed token "<expiry>.<hmac>" the shop sends as "Bearer <token>".
// ponytail: the signing key is random per process, so a redeploy logs the shop out; set a fixed key if that annoys.
// ponytail: failed logins are limited globally (5 a minute); limit per IP if someone keeps hammering it.
@Component
public class ShopAuth {

    static final Duration TTL = Duration.ofHours(12);
    static final int MAX_FAILURES_PER_MINUTE = 5;

    private final byte[] passwordHash;
    private final boolean configured;
    private final byte[] key = new byte[32];
    private final Deque<Long> failures = new ArrayDeque<>();

    public ShopAuth(@Value("${SHOP_PASSWORD:}") String password) {
        this.configured = password.length() >= 8;
        this.passwordHash = sha256(password);
        new SecureRandom().nextBytes(key);
    }

    public record Token(String token, long expiresAt) {
    }

    public Token login(String attempt) {
        if (!configured) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Shop login is off: set SHOP_PASSWORD (8+ characters)");
        }
        if (tooManyFailures()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many wrong passwords, try again in a minute");
        }
        if (attempt == null || !MessageDigest.isEqual(sha256(attempt), passwordHash)) {
            recordFailure();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
        }
        long expiresAt = Instant.now().plus(TTL).getEpochSecond();
        return new Token(expiresAt + "." + sign(expiresAt), expiresAt);
    }

    // Throws 401 unless the Authorization header carries a valid, unexpired token.
    public void require(String authorization) {
        String token = authorization != null && authorization.startsWith("Bearer ") ? authorization.substring(7) : "";
        int dot = token.indexOf('.');
        if (dot > 0) {
            try {
                long expiresAt = Long.parseLong(token.substring(0, dot));
                byte[] given = token.substring(dot + 1).getBytes(StandardCharsets.UTF_8);
                if (expiresAt > Instant.now().getEpochSecond()
                        && MessageDigest.isEqual(given, sign(expiresAt).getBytes(StandardCharsets.UTF_8))) return;
            } catch (NumberFormatException ignored) {
                // fall through to 401
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please log in as the shop");
    }

    private synchronized boolean tooManyFailures() {
        long cutoff = System.currentTimeMillis() - 60_000;
        while (!failures.isEmpty() && failures.peekFirst() < cutoff) failures.pollFirst();
        return failures.size() >= MAX_FAILURES_PER_MINUTE;
    }

    private synchronized void recordFailure() {
        failures.addLast(System.currentTimeMillis());
    }

    private String sign(long expiresAt) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            byte[] sig = mac.doFinal(Long.toString(expiresAt).getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(sig);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] sha256(String s) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
