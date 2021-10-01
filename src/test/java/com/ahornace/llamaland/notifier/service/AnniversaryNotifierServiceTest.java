package com.ahornace.llamaland.notifier.service;

import com.ahornace.llamaland.notifier.model.Citizen;
import com.ahornace.llamaland.notifier.util.ExcludeHelper;
import com.ahornace.llamaland.notifier.service.NotifierService.ProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnniversaryNotifierServiceTest {

    @Test
    void testNotificationForZeroYearsAndZeroNotice() {
        var ns = new AnniversaryNotifierService(0, 0);
        var today = LocalDate.of(2021, Month.SEPTEMBER, 29);
        var citizen = new Citizen("First", "Last", today, "email@test.com");
        var res = ns.process(today, Stream.of(citizen), c -> true);
        Assertions.assertEquals(citizen, res.findFirst().get());
    }

    @Test
    void testWeekendBirthDates() {
        var ns = new AnniversaryNotifierService(0, 1);
        var citizen1 = new Citizen("First", "Boss", LocalDate.of(2021, Month.SEPTEMBER, 19), "email1@test.com");
        var citizen2 = new Citizen("Second", "Boss", LocalDate.of(2021, Month.SEPTEMBER, 18), "email2@test.com");
        var res = ns.process(LocalDate.of(2021, Month.SEPTEMBER, 17), Stream.of(citizen1, citizen2), c -> true);
        assertEquals(Set.of(citizen1, citizen2), res.collect(Collectors.toSet()));
    }

    @Test
    void testDuplicatesAreIgnored() {
        var ns = new AnniversaryNotifierService(0, 1);
        var citizen1 = new Citizen("First", "Boss", LocalDate.of(2021, Month.SEPTEMBER, 19), "email@test.com");
        var citizen2 = new Citizen("Second", "Boss", LocalDate.of(2021, Month.SEPTEMBER, 18), "email@test.com");
        var res = ns.process(LocalDate.of(2021, Month.SEPTEMBER, 17), Stream.of(citizen1, citizen2), c -> true);
        assertEquals(0, res.count());
    }

    @Test
    void testEmailExclusions() throws ProcessingException {
        var ns = new AnniversaryNotifierService(0, 1);
        var citizen1 = new Citizen("First", "Boss", LocalDate.of(2021, Month.SEPTEMBER, 19), "email@test.com");
        var citizen2 = new Citizen("Second", "Boss", LocalDate.of(2021, Month.SEPTEMBER, 18), "test2@test.com");

        var exclusions = AnniversaryNotifierServiceTest.class.getClassLoader()
                .getResource("exclusion_list.txt").getPath();

        var res = ns.process(LocalDate.of(2021, Month.SEPTEMBER, 17), Stream.of(citizen1, citizen2),
                ExcludeHelper.getEmailExclusionFilter(Path.of(exclusions)));
        Assertions.assertEquals(res.findFirst().get(), citizen1);
    }

    @Test
    void testNegativeAnniversary() {
        assertThrows(IllegalArgumentException.class, () -> new AnniversaryNotifierService(-1, 10));
    }

    @Test
    void testNegativeNotice() {
        assertThrows(IllegalArgumentException.class, () -> new AnniversaryNotifierService(Integer.MIN_VALUE, 10));
    }

}
