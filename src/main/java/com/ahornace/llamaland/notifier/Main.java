package com.ahornace.llamaland.notifier;

import com.ahornace.llamaland.notifier.service.AnniversaryNotifierService;
import com.ahornace.llamaland.notifier.service.NotifierService;
import com.ahornace.llamaland.notifier.service.NotifierService.ProcessingException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final int ANNIVERSARY_CELEBRATED = 100;
    private static final int WEEKDAYS_NOTICE = 10;

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Checks for citizens' {@link #ANNIVERSARY_CELEBRATED} anniversary with {@link #WEEKDAYS_NOTICE} weekdays notice.
     * @param args the citizen data file and optionally the exclusion list
     */
    public static void main(String[] args) {
        if (args.length == 0 || args.length > 2) {
            System.err.println(
                    "Incorrect parameters, requiring path(s) to file(s): <citizen_datafile> (<exclusion_list>)");
            System.exit(-1);
        }
        checkInput(args);

        logger.info("Starting data processing");

        NotifierService ns = new AnniversaryNotifierService(ANNIVERSARY_CELEBRATED, WEEKDAYS_NOTICE);
        try {
            ns.process(Path.of(args[0]), args.length > 1 ? Path.of(args[1]) : null)
                    .sorted()
                    .forEach(System.out::println);
        } catch (ProcessingException e) {
            logger.log(Level.SEVERE, "Could not process datafiles", e);
            System.err.println("Error during processing: " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void checkInput(final String[] args) {
        checkFileIsReadable(Path.of(args[0]));
        if (args.length > 1) {
            checkFileIsReadable(Path.of(args[1]));
        }
    }

    private static void checkFileIsReadable(final Path file) {
        if (!Files.isReadable(file)) {
            System.err.println(file + " is not readable");
            System.exit(-1);
        }
    }

}
