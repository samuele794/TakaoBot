package it.discordbot.command.BDO.RSS

import it.discordbot.beans.RSSMessage
import it.discordbot.command.base.RSSScheduler
import it.discordbot.core.JDAController
import it.discordbot.database.filter.BDOPatchInterface
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * Scheduler RSS per le patch di BDO
 * @property bdorssReader BDORSSReader
 * @property bdoPatchInterface BDOPatchInterface
 */
@Service
class BDOPatchRSSScheduler : RSSScheduler {

	private val logger = LoggerFactory.getLogger(BDONewsRSSScheduler::class.java)

	@Autowired
	lateinit var bdorssReader: BDORSSReader

	@Autowired
	lateinit var bdoPatchInterface: BDOPatchInterface


	@Scheduled(fixedRate = 1800000, initialDelay = 1800000)
	fun taskFeedRSSBDOPatch() {

		var rssPatchMessage: RSSMessage? = null
		try {
			rssPatchMessage = bdorssReader.readRSS("https://community.blackdesertonline.com/index.php?forums/patch-notes.5/index.rss")
		} catch (ex: Exception) {
			logger.error("Problemi Lettura FeedRSS News BDO. È Mercoledì?")
		}

		val patchBDO = bdoPatchInterface.getLastBDOPatch()

		if (patchBDO != "") {
			if (bdorssReader.isNew(rssPatchMessage!!.link, patchBDO)) {
				procedurePublish(rssPatchMessage)
                logger.info("PROCESSAZIONE BDO PATCH LINK= " + rssPatchMessage.link)
			}

		} else {
			procedurePublish(rssPatchMessage!!)
		}

	}

	override fun procedurePublish(rssMessage: RSSMessage) {
		val patchMessage = bdorssReader.prepareRSStoMessageEmbed(rssMessage)   //ottieni il messaggio embedded
		val listNewsChannel = bdoPatchInterface.getBDOPatchChannels()
		try {
			publishMessage(patchMessage, listNewsChannel)
		} catch (ex: InsufficientPermissionException) {
			JDAController.logger.debug("""
				Cancellazione canale BDOPatch
				serverId = ${ex.guildId}""".trimIndent())
			bdoPatchInterface.removeBDOPatchChannel(ex.guildId.toString())
		}
		bdoPatchInterface.setLastPatch(rssMessage.link) //salvataggio su db
	}

}