package com.ahornace.llamaland.notifier.model;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Citizen(String firstName, String lastName, LocalDate birthDate, String email)
        implements Comparable<Citizen> {

    public static final String EMAIL_REGEX = "[\\p{Alnum}.]+@[\\p{Alnum}.]+\\.\\p{Alnum}{2,6}";
    private static final String NAME_REGEX = "\\p{Upper}['\\p{Upper}\\p{Lower}]*( \\p{Upper}['\\p{Upper}\\p{Lower}]*)*";

    private static final String FIRST_NAME_GROUP = "firstName";
    private static final String LAST_NAME_GROUP = "lastName";
    private static final String DAY_GROUP = "day";
    private static final String MONTH_GROUP = "month";
    private static final String YEAR_GROUP = "year";
    private static final String EMAIL_GROUP = "email";

    private static final Pattern linePattern = Pattern.compile(
            "(?<" + FIRST_NAME_GROUP + ">" + NAME_REGEX + "),"
                    + "(?<" + LAST_NAME_GROUP + ">" + NAME_REGEX + "),"
                    + "(?<" + DAY_GROUP + ">\\d{2})-(?<" + MONTH_GROUP + ">\\d{2})-(?<" + YEAR_GROUP + ">\\d{4}),"
                    + "(?<" + EMAIL_GROUP + ">" + EMAIL_REGEX + ")");

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Parses the record from the given {@code String}.
     * @param line line in format 'Brown,Bobby,10-11-1950,bobby.brown@ilovellamaland.com'
     * @return citizen if parsing successful, empty otherwise
     */
    public static Optional<Citizen> parse(final String line) {
        Matcher matcher = linePattern.matcher(line);
        if (matcher.matches()) {
            var citizen = new Citizen(matcher.group(FIRST_NAME_GROUP), matcher.group(LAST_NAME_GROUP),
                    getBirthDate(matcher), matcher.group(EMAIL_GROUP));
            return Optional.of(citizen);
        }
        logger.log(Level.WARNING, "Cannot parse citizen from line: {0}", line);
        return Optional.empty();
    }

    private static LocalDate getBirthDate(final Matcher matcher) {
        return LocalDate.of(
                Integer.parseInt(matcher.group(YEAR_GROUP)),
                Integer.parseInt(matcher.group(MONTH_GROUP)),
                Integer.parseInt(matcher.group(DAY_GROUP)));
    }

    @Override
    public int compareTo(final Citizen other) {
        return Comparator.comparing(Citizen::lastName)
                .thenComparing(Citizen::firstName)
                .thenComparing(Citizen::email)
                .compare(this, other);
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                '}';
    }
}
