package com.ahornace.llamaland.notifier.service;

import com.ahornace.llamaland.notifier.model.Citizen;

import java.io.Serial;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Service for finding citizens to be notified.
 */
public interface NotifierService {

    class ProcessingException extends Exception {

        @Serial
        private static final long serialVersionUID = 1;

        public ProcessingException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Filters citizens to be notified from {@code citizenDatafile} excluding the ones in {@code exclusionDatafile}.
     * @param citizenDatafile file containing data about citizens
     * @param exclusionDatafile file identifying citizens to excluded from being notified
     * @return stream of citizens to be notified
     * @throws ProcessingException if datafiles could not be processed
     */
    Stream<Citizen> process(final Path citizenDatafile, final Path exclusionDatafile) throws ProcessingException;

}
