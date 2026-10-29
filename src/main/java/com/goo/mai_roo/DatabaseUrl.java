package com.goo.mai_roo;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Lets DATABASE_URL be pasted the way Neon/Railway hand it out:
//   postgresql://user:pass@host/db?sslmode=require        (Neon's default)
//   jdbc:postgresql://user:pass@host/db?sslmode=require
//   jdbc:postgresql://host/db?user=...&password=...        (already JDBC, left as is)
// The Postgres JDBC driver only takes the last form, so the others are rewritten to it.
// Registered in META-INF/spring.factories; runs after application.properties and .env are loaded.
public class DatabaseUrl implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
        String url = env.getProperty("DATABASE_URL");
        if (url == null || url.isBlank()) return;
        env.getPropertySources().addFirst(new MapPropertySource("databaseUrl", Map.of("DATABASE_URL", toJdbc(url))));
    }

    static String toJdbc(String url) {
        String u = url.strip().replaceFirst("^jdbc:", "");
        if (!u.startsWith("postgres://") && !u.startsWith("postgresql://")) return url.strip();

        URI uri = URI.create(u);
        StringBuilder jdbc = new StringBuilder("jdbc:postgresql://").append(uri.getHost());
        if (uri.getPort() > 0) jdbc.append(':').append(uri.getPort());
        if (uri.getRawPath() != null) jdbc.append(uri.getRawPath());

        List<String> params = new ArrayList<>();
        if (uri.getRawQuery() != null) params.add(uri.getRawQuery());
        String userInfo = uri.getRawUserInfo();
        if (userInfo != null) {
            int colon = userInfo.indexOf(':');
            params.add("user=" + (colon < 0 ? userInfo : userInfo.substring(0, colon)));
            if (colon >= 0) params.add("password=" + userInfo.substring(colon + 1));
        }
        if (!params.isEmpty()) jdbc.append('?').append(String.join("&", params));
        return jdbc.toString();
    }
}
