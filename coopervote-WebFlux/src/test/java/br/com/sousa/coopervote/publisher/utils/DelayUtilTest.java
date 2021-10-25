package br.com.sousa.coopervote.publisher.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DelayUtilTest {

    private static final int SIXTY_MINUTES = 60;
    private static final int SIXTY_MINUTES_IN_MILLIS = 3600000;

    @Test
    void convertToMilis() {
        int millis = DelayUtil.convertToMilis(SIXTY_MINUTES);
        assertEquals(SIXTY_MINUTES_IN_MILLIS, millis);
    }

    @Test
    void convertToMilis_whenConvertionExcedIntegerLenth() {
        int millis = DelayUtil.convertToMilis(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, millis);
    }

    @Test
    void getDurationInMillis() {
        LocalDateTime fim  = LocalDateTime.now().plusMinutes(SIXTY_MINUTES);
        assertTrue(DelayUtil.getDurationInMillis(fim) <= SIXTY_MINUTES_IN_MILLIS);
    }

    @Test
    void getDurationInMillis_whenTimeExpired() {
        LocalDateTime fim  = LocalDateTime.now().minusMinutes(SIXTY_MINUTES);
        assertEquals(0, DelayUtil.getDurationInMillis(fim));
    }
}