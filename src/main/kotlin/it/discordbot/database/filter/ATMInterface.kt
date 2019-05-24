package it.discordbot.database.filter

import it.discordbot.beans.ServerToChannel
import it.discordbot.database.repository.RSSLinkRepository
import it.discordbot.database.repository.ServerDiscordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Classe di interfacciamento al db per le funzioni di ATM
 * @property serverDiscordRepository ServerDiscordRepository
 * @property rssLinkRepository RSSLinkRepository
 */
@Scope("singleton")
@Component
class ATMInterface {

	@Autowired
	lateinit var serverDiscordRepository: ServerDiscordRepository

	@Autowired
	lateinit var rssLinkRepository: RSSLinkRepository

	fun setATMAlertChannel(serverID: String, channelID: String) {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		serverDiscord.atmAlertIDChannel = channelID
		serverDiscordRepository.save(serverDiscord)
	}

	fun removeATMAlertChannel(serverID: String): String? {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		val atmChannelRemoveID = serverDiscord.atmAlertIDChannel
		serverDiscord.atmAlertIDChannel = null
		serverDiscordRepository.save(serverDiscord)
		return atmChannelRemoveID
	}

	fun getATMAlertChannels(): ArrayList<ServerToChannel> {
		return serverDiscordRepository.getAllATMAlertChannels()
	}

	fun setLastATMAlert(url: String) {
		rssLinkRepository.lastATMNewsLink = url
	}

	fun getLastATMAlert(): String? {
		return rssLinkRepository.lastATMNewsLink
	}

}