package it.discordbot.command.tpl.atmAlert

import it.discordbot.beans.RSSMessage
import it.discordbot.core.JDAController
import it.discordbot.database.interfaces.ATMInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Scope("singleton")
@Service
class ATMRSSScheduler {

	companion object {
		const val ATM_RSS_LINK = "https://www.atm.it/_layouts/atm/apps/PublishingRSS.aspx?web=388a6572-890f-4e0f-a3c7-a3dd463f7252&c=News%20Infomobilita"
	}

	@Autowired
	lateinit var atmrssReader: ATMRSSReader

	@Autowired
	lateinit var atmInterface: ATMInterface

	@Scheduled(fixedRate = 1800000, initialDelay = 1800000)
	fun taskFeedRSSATM() {
		val message = atmrssReader.readRSS(ATM_RSS_LINK)

		val lastATMAlert = atmInterface.getLastATMAlert()

		if (lastATMAlert != null) {
			if (lastATMAlert != message.link) {
				publish(message)
				atmInterface.setLastATMAlert(message.link)
			}
		} else {
			publish(message)
			atmInterface.setLastATMAlert(message.link)
		}

	}

	private fun publish(message: RSSMessage) {
		val serverList = atmInterface.getATMAlertChannels()
		val messageEmbed = atmrssReader.prepareRSStoEmbeddedMessage(message)
		for (server in serverList) {
			JDAController.jda.getGuildById(server.serverID)
					.getTextChannelById(server.channelID)
					.sendMessage(messageEmbed)
					.queue()
		}

	}

}