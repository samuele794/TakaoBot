package it.discordbot.database.interfaces

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.discordbot.beans.ServerToChannel
import it.discordbot.database.repository.RSSLinkRepository
import it.discordbot.database.repository.ServerDiscordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Scope("singleton")
@Component
class BDONewsInterface {

	@Autowired
	lateinit var serverDiscordRepository: ServerDiscordRepository

	@Autowired
	lateinit var rssLinkRepository: RSSLinkRepository

	fun setBDONewsChannel(serverID:String , channelID: String){
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

	fun getBDONewsChannels(): ArrayList<ServerToChannel>{
		return serverDiscordRepository.getAllBDONewsChannels()
	}

	fun getBDONewsList(): ArrayList<String>{

		val bdoRSSNews = rssLinkRepository.getFirstById().lastNewsBDO

		if (bdoRSSNews == null){
			return ArrayList()
		}else{
			return jacksonObjectMapper().readValue(bdoRSSNews)
		}

	}

	fun setBDONewsList(list: ArrayList<String>){
		rssLinkRepository.getFirstById().apply {
			lastNewsBDO = jacksonObjectMapper().writeValueAsString(list)
			rssLinkRepository.save(this)
		}
	}
}