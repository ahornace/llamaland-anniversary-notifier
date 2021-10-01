package com.ahornace.llamaland.notifier.util;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

public class TemporalUtil {

    private TemporalUtil() {
    }

    public static TemporalAdjuster minusWeekDays(final int days) {
        if (days < 0) {
            throw new IllegalArgumentException("Cannot subtract negative week days");
        }
        return temporal -> {
            // let's reuse previousWeekDay() for last week to avoid handling of corner cases
            int weeksButLast = (days - 1) / 5;
            Temporal temp = temporal.minus(weeksButLast, ChronoUnit.WEEKS);
            int daysInLastWeek = (days - 1) % 5 + 1;
            for (int i = 0; i < daysInLastWeek; i++) {
                temp = temp.with(previousWeekDay());
            }
            return temp;
        };
    }

    public static TemporalAdjuster previousWeekDay() {
        return temporal -> switch (DayOfWeek.from(temporal)) {
            case MONDAY -> temporal.minus(3, ChronoUnit.DAYS);
            case SUNDAY -> temporal.minus(2, ChronoUnit.DAYS);
            default -> temporal.minus(1, ChronoUnit.DAYS);
        };
    }

}
