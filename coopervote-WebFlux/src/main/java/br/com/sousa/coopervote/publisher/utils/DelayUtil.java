package br.com.sousa.coopervote.publisher.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public final class DelayUtil {

    private DelayUtil(){}
    public static Integer convertToMilis(int minutes) {
        Long millis = Duration.ofMinutes(minutes).toMillis();
        return millis > Integer.MAX_VALUE ? Integer.MAX_VALUE : Integer.valueOf(millis.toString());
    }

    public static Integer getDurationInMillis(LocalDateTime fim) {
        Long millis = Duration.between(LocalDateTime.now(), fim).toMillis();
        return millis > Integer.MAX_VALUE ? Integer.MAX_VALUE :
                millis < 0 ? 0 : Integer.valueOf(millis.toString());
    }
}
