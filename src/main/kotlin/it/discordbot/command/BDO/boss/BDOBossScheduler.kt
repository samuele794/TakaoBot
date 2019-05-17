package it.discordbot.command.BDO.boss

import it.discordbot.database.filter.BDOBossInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

/**
 * Classe per la schedulazione dei boss
 * @property bdoBossInterface BDOBossInterface
 * @property bossUtil BossUtil
 */
@Scope("singleton")
@Service
class BDOBossScheduler {

	@Autowired
	lateinit var bdoBossInterface: BDOBossInterface

	@Autowired
	lateinit var bossUtil: BossUtil

	/**
	 * Job per la schedulazione dei boss
	 */
	@Scheduled(cron = "0 0/5 * * * *")
	fun bossJob() {
		val time = LocalDateTime.now()
		val list = bdoBossInterface.getBDOBossList()
		bossUtil.getDayBosses(time.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH), list).apply {
			bossUtil.publishBoss(time.hour, time.minute, this)
		}

	}
}