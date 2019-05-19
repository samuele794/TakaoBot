package it.discordbot.core

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Logger Unico per il bot
 */
@Component
class TakaoLog {
	companion object {
		private var logger = LoggerFactory.getLogger(TakaoLog::class.java)

		fun logDebug(message: String) {
			logger.debug(message)
		}

		fun logInfo(message: String) {
			logger.info(message)
		}

		fun logError(message: String) {
			logger.error(message)
		}
	}
}