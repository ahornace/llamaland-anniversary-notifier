package com.ahornace.llamaland.notifier.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemporalUtilTest {

    @Test
    void testPreviousWeekDayForMonday() {
        var result = LocalDate.of(2021, Month.SEPTEMBER, 27).with(TemporalUtil.previousWeekDay());
        assertEquals(LocalDate.of(2021, Month.SEPTEMBER, 24), result);
    }

    @Test
    void testPreviousWeekDayForSundayWithYearSkip() {
        var result = LocalDate.of(2022, Month.JANUARY, 1).with(TemporalUtil.previousWeekDay());
        assertEquals(LocalDate.of(2021, Month.DECEMBER, 31), result);
    }

    @Test
    void testPreviousWeekDayForSaturdayWithMonthSkip() {
        var result = LocalDate.of(2021, Month.MAY, 1).with(TemporalUtil.previousWeekDay());
        assertEquals(LocalDate.of(2021, Month.APRIL, 30), result);
    }

    @ParameterizedTest
    @MethodSource("getDatesWithWeekDaysBetweenTuesdayAndFriday")
    void testPreviousWeekDayForDaysBetweenTuesdayAndFriday(final LocalDate date) {
        assertEquals(date.minusDays(1), date.with(TemporalUtil.previousWeekDay()));
    }

    @Test
    void testMinusWeekDaysForZero() {
        var date = LocalDate.of(2021, Month.SEPTEMBER, 27);
        assertEquals(date, date.with(TemporalUtil.minusWeekDays(0)));
    }

    @Test
    void testMinusWeekDaysForNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> LocalDate.of(2021, Month.SEPTEMBER, 27)
                .with(TemporalUtil.minusWeekDays(-15)));
    }

    @Test
    void testMinusOneWeekDayOnMondayReturnsFriday() {
        var result = LocalDate.of(2021, Month.SEPTEMBER, 27).with(TemporalUtil.minusWeekDays(1));
        assertEquals(LocalDate.of(2021, Month.SEPTEMBER, 24), result);
    }

    @Test
    void testMinusWeekDaysOverOneWeek() {
        var result = LocalDate.of(2021, Month.SEPTEMBER, 27).with(TemporalUtil.minusWeekDays(6));
        assertEquals(LocalDate.of(2021, Month.SEPTEMBER, 17), result);
    }

    @Test
    void testMinusWeekDaysOverOneMonth() {
        var result = LocalDate.of(2021, Month.SEPTEMBER, 27).with(TemporalUtil.minusWeekDays(20));
        assertEquals(LocalDate.of(2021, Month.AUGUST, 30), result);
    }

    @Test
    void testMinusWeekDaysForMaxIntDoesNotThrowException() {
        // there should be a proper assertEquals...
        LocalDate.now().with(TemporalUtil.minusWeekDays(Integer.MAX_VALUE - 1));
    }

    @Test
    void testMinusFiveThousandWeekDays() {
        // compared with https://www.timeanddate.com/date/weekdayadd.html?d1=29&m1=09&y1=2021&
        var result = LocalDate.of(2021, Month.SEPTEMBER, 29).with(TemporalUtil.minusWeekDays(5000));
        assertEquals(LocalDate.of(2002, Month.JULY, 31), result);
    }

    private static List<LocalDate> getDatesWithWeekDaysBetweenTuesdayAndFriday() {
        return List.of(
                LocalDate.of(2021, Month.OCTOBER, 8),
                LocalDate.of(2021, Month.OCTOBER, 14),
                LocalDate.of(2022, Month.JUNE, 8),
                LocalDate.of(2022, Month.NOVEMBER, 1)
        );
    }

}
