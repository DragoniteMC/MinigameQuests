package com.ericlam.mc.questtest;

import java.sql.Timestamp;
import java.time.Duration;

public class DurationTest {

    public static void main(String[] args) {
        var oneDay = Duration.ofDays(1);
        var date = new Timestamp(1654247206150L).toLocalDateTime();
        System.out.println("last finished: "+date.toString());
        var cool = date.plus(oneDay);
        System.out.println("cool: "+cool.toString());
        System.out.println("diff: "+formatDuration(Duration.between(cool, date)));
    }

    static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        return (seconds >= 0 ? "" : "-") + String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
    }
}
