package com.settp.id.core.service;

import com.settp.id.core.model.IdentityStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

public class SecurityLogger {
    private static final Logger logger = Logger.getLogger(SecurityLogger.class.getName());
    private static final String LOG_FILE = "security.log";

    static {
        try {
            logger.setUseParentHandlers(false);

            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new LogFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
    } catch (IOException e) {
            System.err.println("[ERROR] Could not initialise security logger");
        }
    }

    public static class LogFormatter extends Formatter {
        private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public String format(LogRecord record) {
            return String.format("[%s] [%s] - %s%n", LocalDateTime.now().format(dtFormatter), record.getLevel(), record.getMessage());
        }
    }

    public static void logCreation(String uuid) {
        logger.info("ID CREATED: Assigned UUID: " + uuid);
    }

    public static void logStatusUpdate(String uuid, IdentityStatus newStatus) {
        logger.info(String.format("STATUS UPDATED | UUID: %s | Status updated to %s", uuid, newStatus));
    }

    public static void logAttributeUpdate(String uuid, String attributeKey, String attributeValue) {
        logger.info(String.format("ATTRIBUTE UPDATED | UUID: %s | %s updated to %s", uuid, attributeKey, attributeValue));
    }

    public static void logAccess(String uuid, String organisation) {
        logger.info(String.format("DATA ACCESSED | UUID: %s | ID Viewed by %s", uuid, organisation));
    }

    public static void logUnauthorisedAttempt(String uuid, String organisation, String reason) {
        logger.warning(String.format("UNAUTHORISED ATTEMPT | UUID: %s | %s attempted to, %s", uuid, organisation, reason));
    }
}
