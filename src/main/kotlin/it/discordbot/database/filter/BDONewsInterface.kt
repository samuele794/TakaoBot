package it.discordbot.database.filter

import it.discordbot.beans.ServerToChannel
import it.discordbot.database.repository.RSSLinkRepository
import it.discordbot.database.repository.ServerDiscordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Classe di interfacciamento al db per le funzioni di BDO News
 * @property serverDiscordRepository ServerDiscordRepository
 * @property rssLinkRepository RSSLinkRepository
 */
@Scope("singleton")
@Component
class BDONewsInterface {

	@Autowired
	lateinit var serverDiscordRepository: ServerDiscordRepository

	@Autowired
	lateinit var rssLinkRepository: RSSLinkRepository

	fun setBDONewsChannel(serverID: String, channelID: String) {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		serverDiscord.bdoNewsIDChannel = channelID
		serverDiscordRepository.save(serverDiscord)
	}

	fun removeBDONewsChannel(serverID: String): String? {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		val newsChannelRemoveID = serverDiscord.bdoNewsIDChannel
		serverDiscord.bdoNewsIDChannel = null
		serverDiscordRepository.save(serverDiscord)
		return newsChannelRemoveID
	}

	fun getBDONewsChannels(): ArrayList<ServerToChannel> {
		return serverDiscordRepository.getAllBDONewsChannels()
	}

	fun getLastBDONews(): String {

		val bdoRSSNews = rssLinkRepository.lastBDONewsLink

		if (bdoRSSNews == null) {
			return ""
		} else {
			return bdoRSSNews
		}

	}

	fun setBDONews(link: String) {
		rssLinkRepository.lastBDONewsLink = link
	}
}