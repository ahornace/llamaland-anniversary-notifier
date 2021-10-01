package com.ahornace.llamaland.notifier.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CitizenTest {

    @ParameterizedTest
    @ValueSource(strings = {
            ",Bobby,10-11-1950,bobby.brown@ilovellamaland.com",
            "Brown,,10-11-1950,bobby.brown@ilovellamaland.com",
            "Brown,Bobby,,bobby.brown@ilovellamaland.com",
            "Brown,Bobby,10-11-1950,",
            "Brown,Bobby,10-11-1,bobby.brown@ilovellamaland.com",
            "Brown,Bobby,10-11-1950,bobby.brown@ilovella@maland.com",
            "Brown   ,Bobby,10-11-1950,bobby.brown@ilovella@maland.com",
            "Brown   ,Bobby,10-11-1950\t,bobby.brown@ilovella@maland.com",
    })
    void testParseReturnsEmptyForInvalidInput(final String line) {
        assertTrue(Citizen.parse(line).isEmpty());
    }

    @Test
    void testParse() {
        Optional<Citizen> citizen = Citizen.parse("Brown,Bobby,10-11-1950,bobby.brown@ilovellamaland.com");
        assertTrue(citizen.isPresent());
        assertEquals("Brown", citizen.get().firstName());
        assertEquals("Bobby", citizen.get().lastName());
        assertEquals(LocalDate.of(1950, 11, 10), citizen.get().birthDate());
        assertEquals("bobby.brown@ilovellamaland.com", citizen.get().email());
    }

    @Test
    void testParseNameWithApostrophe() {
        Optional<Citizen> citizen = Citizen.parse("O'Rourke,Betsy,28-02-1900,betsy@heyitsme.com");
        assertTrue(citizen.isPresent());
        assertEquals("O'Rourke", citizen.get().firstName());
    }

    @Test
    void testParseNameWithSpace() {
        Optional<Citizen> citizen = Citizen.parse("Von Tappo,Alfredo,01-01-1920,alfie@vontappo.llama.land");
        assertTrue(citizen.isPresent());
        assertEquals("Von Tappo", citizen.get().firstName());
    }

}
