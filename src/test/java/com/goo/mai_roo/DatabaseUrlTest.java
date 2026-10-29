package com.goo.mai_roo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseUrlTest {

    @Test
    void neonAndRailwayFormatsBecomeJdbc() {
        String want = "jdbc:postgresql://ep-x.neon.tech/neondb?sslmode=require&user=me&password=secret";
        assertEquals(want, DatabaseUrl.toJdbc("postgresql://me:secret@ep-x.neon.tech/neondb?sslmode=require"));
        assertEquals(want, DatabaseUrl.toJdbc("jdbc:postgresql://me:secret@ep-x.neon.tech/neondb?sslmode=require"));
        assertEquals("jdbc:postgresql://h:5433/db?user=me", DatabaseUrl.toJdbc(" postgres://me@h:5433/db "));
    }

    @Test
    void jdbcUrlsWithoutUserInfoAreKept() {
        String jdbc = "jdbc:postgresql://h/db?user=me&password=secret";
        assertEquals(jdbc, DatabaseUrl.toJdbc(jdbc));
        assertEquals("jdbc:h2:mem:x", DatabaseUrl.toJdbc("jdbc:h2:mem:x"));
    }
}
