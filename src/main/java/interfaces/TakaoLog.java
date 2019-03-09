package interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import starter.Start;

public class TakaoLog {
	private static Logger logger = LoggerFactory.getLogger(Start.class);

	public static void logInfo(String message) {
		logger.info(message);
	}

	public static void logDebug(String message) {
		logger.debug(message);
	}

	public static void logError(String message) {
		logger.error(message);
	}
}
