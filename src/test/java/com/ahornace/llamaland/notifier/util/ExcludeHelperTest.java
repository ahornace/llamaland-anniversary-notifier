package com.ahornace.llamaland.notifier.util;

import com.ahornace.llamaland.notifier.model.Citizen;
import com.ahornace.llamaland.notifier.service.NotifierService.ProcessingException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcludeHelperTest {

    @Test
    void testExcludeSimpleScenario() throws ProcessingException {
        var path = ExcludeHelperTest.class.getClassLoader().getResource("exclusion_list.txt").getPath();
        var filter = ExcludeHelper.getEmailExclusionFilter(Path.of(path));

        var dummyCitizen = new Citizen("First", "Last", LocalDate.of(2021, Month.SEPTEMBER, 28), "test@test.com");
        assertFalse(filter.test(dummyCitizen));
        dummyCitizen = new Citizen("First", "Last", LocalDate.of(2021, Month.SEPTEMBER, 28), "test2@test.com");
        assertFalse(filter.test(dummyCitizen));
        dummyCitizen = new Citizen("First", "Last", LocalDate.of(2021, Month.SEPTEMBER, 28),
                "invalid_email_should_be_ignored");
        assertTrue(filter.test(dummyCitizen));
        dummyCitizen = new Citizen("First", "Last", LocalDate.of(2021, Month.SEPTEMBER, 28), "other@email.com");
        assertTrue(filter.test(dummyCitizen));
    }

}
