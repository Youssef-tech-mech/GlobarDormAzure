package ntu.service_centric.global_dorm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message, Throwable ex) {
        logger.error(message, ex);
    }

    public static void logWarning(String message) {
        logger.warn(message);
    }
}
