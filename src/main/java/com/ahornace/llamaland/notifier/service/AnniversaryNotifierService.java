package com.ahornace.llamaland.notifier.service;

import com.ahornace.llamaland.notifier.model.Citizen;
import com.ahornace.llamaland.notifier.util.ExcludeHelper;
import com.ahornace.llamaland.notifier.util.TemporalUtil;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Notifier that filters citizens with upcoming anniversaries.
 */
public class AnniversaryNotifierService implements NotifierService {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final int anniversaryCelebrated;
    private final int weekDaysNotice;

    public AnniversaryNotifierService(final int anniversaryCelebrated, final int weekDaysNotice) {
        if (anniversaryCelebrated < 0) {
            throw new IllegalArgumentException("Cannot celebrate negative anniversary");
        }
        if (weekDaysNotice < 0) {
            throw new IllegalArgumentException("Cannot give negative notice");
        }
        this.anniversaryCelebrated = anniversaryCelebrated;
        this.weekDaysNotice = weekDaysNotice;
    }

    @Override
    public Stream<Citizen> process(final Path citizenDatafile, final Path exclusionDatafile) throws ProcessingException {
        Predicate<Citizen> exclusionFilter;
        if (exclusionDatafile == null) {
            exclusionFilter = c -> true;
        } else {
            exclusionFilter = ExcludeHelper.getEmailExclusionFilter(exclusionDatafile);
        }
        try (var stream = Files.lines(citizenDatafile, StandardCharsets.UTF_8)) {
            LocalDate today = LocalDate.now();
            return process(today, stream.map(Citizen::parse).flatMap(Optional::stream), exclusionFilter);
        } catch (IOException e) {
            throw new ProcessingException("Could not process citizen datafile", e);
        }
    }

    //@VisibleForTesting
    Stream<Citizen> process(
            final LocalDate today,
            final Stream<Citizen> citizens,
            final Predicate<Citizen> customFilter
    ) {
        return citizens.collect(Collectors.groupingBy(Citizen::email))
                .values()
                .parallelStream()
                .filter(l ->  {
                    if (l.size() != 1) {
                        logger.log(Level.FINEST, "Excluding citizens with duplicate emails: {0}", l);
                        return false;
                    }
                    return true;
                })
                .flatMap(Collection::stream)
                .filter(customFilter)
                .filter(c -> shouldBeNotified(today, c));
    }

    //@VisibleForTesting
    boolean shouldBeNotified(final LocalDate today, final Citizen citizen) {
        LocalDate notificationDate = citizen.birthDate().plusYears(anniversaryCelebrated)
                .with(TemporalUtil.minusWeekDays(weekDaysNotice));

        boolean shouldBeNotified = today.equals(notificationDate);
        if (!shouldBeNotified) {
            logger.log(Level.FINEST, "Excluding {0} as they should be notified on {1}",
                    new Object[] {citizen, notificationDate});
        }
        return shouldBeNotified;
    }

}
