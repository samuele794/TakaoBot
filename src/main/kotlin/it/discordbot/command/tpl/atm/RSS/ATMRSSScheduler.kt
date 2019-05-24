package it.discordbot.command.tpl.atm.RSS

import it.discordbot.beans.RSSMessage
import it.discordbot.beans.ServerToChannel
import it.discordbot.command.pattern.RSSScheduler
import it.discordbot.core.JDAController
import it.discordbot.database.filter.ATMInterface
import net.dv8tion.jda.core.entities.MessageEmbed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * Classe per la schedulaziooe dei messaggi RSS di ATM
 * @property atmrssReader ATMRSSReader
 * @property atmInterface ATMInterface
 */
@Scope("singleton")
@Service
class ATMRSSScheduler : RSSScheduler {

	companion object {
		const val ATM_RSS_LINK = "https://www.atm.it/_layouts/atm/apps/PublishingRSS.aspx?web=388a6572-890f-4e0f-a3c7-a3dd463f7252&c=News%20Infomobilita"
	}

	@Autowired
	lateinit var atmrssReader: ATMRSSReader

	@Autowired
	lateinit var atmInterface: ATMInterface

	@Scheduled(fixedRate = 900000, initialDelay = 900000)
	fun taskFeedRSSATM() {
		val message = atmrssReader.readRSS(ATM_RSS_LINK)

		val lastATMAlert = atmInterface.getLastATMAlert()

		if (lastATMAlert != null) {
			if (lastATMAlert != message.link) {
				procedurePublish(message)
			}
		} else {
			procedurePublish(message)
		}
	}

	override fun procedurePublish(rssMessage: RSSMessage) {
		val serverList = atmInterface.getATMAlertChannels()
		val messageEmbed = atmrssReader.prepareRSStoMessageEmbed(rssMessage)
		publishMessage(messageEmbed, serverList)
		atmInterface.setLastATMAlert(rssMessage.link)

	}

	override fun publishMessage(message: MessageEmbed, serversToChannel: ArrayList<ServerToChannel>) {
		for (obj in serversToChannel) {
			JDAController.jda.getGuildById(obj.serverID)
					.getTextChannelById(obj.channelID)
					.sendMessage(message)
					.queue()
		}
	}

}