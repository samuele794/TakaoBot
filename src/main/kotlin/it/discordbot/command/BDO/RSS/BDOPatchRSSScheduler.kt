package it.discordbot.command.BDO.RSS

import it.discordbot.beans.RSSMessage
import it.discordbot.beans.ServerToChannel
import it.discordbot.command.pattern.RSSScheduler
import it.discordbot.core.JDAController
import it.discordbot.core.TakaoLog
import it.discordbot.database.filter.BDOPatchInterface
import net.dv8tion.jda.core.entities.MessageEmbed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
class BDOPatchRSSScheduler : RSSScheduler {

	@Autowired
	lateinit var bdorssReader: BDORSSReader

	@Autowired
	lateinit var bdoPatchInterface: BDOPatchInterface


	@Scheduled(fixedRate = 1800000, initialDelay = 1800000)
	fun taskFeedRSSBDOPatch() {

		val rssPatchMessage = bdorssReader.readRSS("https://community.blackdesertonline.com/index.php?forums/patch-notes.5/index.rss")
		val patchBDO = bdoPatchInterface.getLastBDOPatch()

		if (patchBDO != "") {
			if (bdorssReader.isNew(rssPatchMessage.link, patchBDO)) {
				procedurePublish(rssPatchMessage)
				TakaoLog.logInfo("PROCESSAZIONE BDO PATCH LINK= " + rssPatchMessage.link)
			}

		} else {
			procedurePublish(rssPatchMessage)
		}

	}

	override fun procedurePublish(rssMessage: RSSMessage) {
		val patchMessage = bdorssReader.prepareRSStoMessageEmbed(rssMessage)   //ottieni il messaggio embedded
		val listNewsChannel = bdoPatchInterface.getBDOPatchChannels()
		publishMessage(patchMessage, listNewsChannel)
		bdoPatchInterface.setLastPatch(rssMessage.link) //salvataggio su db
	}

	override fun publishMessage(message: MessageEmbed, serversToChannel: ArrayList<ServerToChannel>) {
		for (obj in serversToChannel) {
			JDAController.jda.getGuildById(obj.serverID)
					.getTextChannelById(obj.channelID)
					.sendMessage(message).queue()
		}
	}

}