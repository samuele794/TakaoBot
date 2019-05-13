package it.discordbot.command.BDO.RSS

import it.discordbot.beans.RSSMessage
import it.discordbot.beans.ServerToChannel
import it.discordbot.core.JDAController
import it.discordbot.database.interfaces.BDONewsInterface
import net.dv8tion.jda.core.entities.MessageEmbed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
class BDONewsRSSScheduler {

	@Autowired
	lateinit var bdorssReader: BDORSSReader

	@Autowired
	lateinit var bdoNewsInterface: BDONewsInterface


	@Scheduled(fixedRate = 1800000, initialDelay = 1800000)
	fun taskFeedRSSBDONews() {
		val rssNewsMessage = bdorssReader.readRSS("https://community.blackdesertonline.com/index.php?forums/news-announcements.181/index.rss")

		var newsBDOList = bdoNewsInterface.getBDONewsList()
		if (newsBDOList.size != 0) {
			if (newsBDOList.indexOf(rssNewsMessage.link) == -1) {
				procedurePublish(rssNewsMessage, newsBDOList)
			}
		} else {
			newsBDOList = ArrayList()
			procedurePublish(rssNewsMessage, newsBDOList)
		}
	}

	private fun procedurePublish(rssNewsMessage: RSSMessage, newsBDOList: ArrayList<String>) {
		val newsMessage = bdorssReader.prepareRSStoEmbeddedMessage(rssNewsMessage)   //ottieni il messaggio embedded
		val listNewsChannel = bdoNewsInterface.getBDONewsChannels()         //ottieni la delle ultime news già pubblicate
		publishMessage(newsMessage, listNewsChannel)                                         //publish del messaggio
		newsBDOList.add(rssNewsMessage.link)                                          //aggiunta dell'ultima news alla lista
		bdoNewsInterface.setBDONewsList(newsBDOList)                                           //salvataggio su db
	}

	private fun publishMessage(newsMessage: MessageEmbed, servers: ArrayList<ServerToChannel>) {
		for (obj in servers) {
			JDAController.jda.getGuildById(obj.serverID)
					.getTextChannelById(obj.channelID)
					.sendMessage(newsMessage).queue()
		}
	}

}