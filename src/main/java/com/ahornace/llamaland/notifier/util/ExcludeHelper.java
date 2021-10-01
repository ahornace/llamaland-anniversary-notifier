package com.ahornace.llamaland.notifier.util;

import com.ahornace.llamaland.notifier.model.Citizen;
import com.ahornace.llamaland.notifier.service.NotifierService.ProcessingException;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ExcludeHelper {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private ExcludeHelper() {
    }

    public static Predicate<Citizen> getEmailExclusionFilter(final Path pathToExclusionList) throws ProcessingException {
        try (var stream = Files.lines(pathToExclusionList, StandardCharsets.UTF_8)) {
            Set<String> exclusions = stream.filter(l -> l.matches(Citizen.EMAIL_REGEX))
                    .collect(Collectors.toSet());
            return c -> {
                var excluded = exclusions.contains(c.email());
                if (excluded) {
                    logger.log(Level.FINEST, "{0} excluded due to being on the exclusion list", c);
                    return false;
                }
                return true;
            };
        } catch (IOException e) {
            throw new ProcessingException("Could not process exclusion list", e);
        }
    }

}
