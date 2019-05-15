package it.discordbot.command.BDO.RSS

import it.discordbot.beans.RSSMessage
import it.discordbot.beans.ServerToChannel
import it.discordbot.command.pattern.RSSScheduler
import it.discordbot.core.JDAController
import it.discordbot.database.filter.BDONewsInterface
import net.dv8tion.jda.core.entities.MessageEmbed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
class BDONewsRSSScheduler : RSSScheduler {

	@Autowired
	lateinit var bdorssReader: BDORSSReader

	@Autowired
	lateinit var bdoNewsInterface: BDONewsInterface


	@Scheduled(fixedRate = 1800000, initialDelay = 1800000)
	fun taskFeedRSSBDONews() {
		val rssNewsMessage = bdorssReader.readRSS("https://community.blackdesertonline.com/index.php?forums/news-announcements.181/index.rss")

		val newsBDO = bdoNewsInterface.getLastBDONews()
		if (newsBDO != "") {
			if (bdorssReader.isNew(rssNewsMessage.link, newsBDO)) {
				procedurePublish(rssNewsMessage)
			}
		} else {
			procedurePublish(rssNewsMessage)
		}
	}

	override fun procedurePublish(rssMessage: RSSMessage) {
		val newsMessage = bdorssReader.prepareRSStoMessageEmbed(rssMessage) //ottieni il messaggio embedded
		val listNewsChannel = bdoNewsInterface.getBDONewsChannels()
		publishMessage(newsMessage, listNewsChannel)
		bdoNewsInterface.setBDONews(rssMessage.link) //salvataggio su db
	}

	override fun publishMessage(message: MessageEmbed, serversToChannel: ArrayList<ServerToChannel>) {
		for (obj in serversToChannel) {
			JDAController.jda.getGuildById(obj.serverID)
					.getTextChannelById(obj.channelID)
					.sendMessage(message).queue()
		}
	}

}