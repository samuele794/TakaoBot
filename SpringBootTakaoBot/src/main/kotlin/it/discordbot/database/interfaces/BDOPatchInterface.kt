package it.discordbot.database.interfaces

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.discordbot.beans.ServerToChannel
import it.discordbot.database.repository.RSSLinkRepository
import it.discordbot.database.repository.ServerDiscordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller

@Scope("singleton")
@Component
class BDOPatchInterface {

	@Autowired
	lateinit var serverDiscordRepository: ServerDiscordRepository

	@Autowired
	lateinit var rssLinkRepository: RSSLinkRepository

	fun setBDOPatchChannel(serverID:String , channelID: String){
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		serverDiscord.bdoPatchIDChannel = channelID
		serverDiscordRepository.save(serverDiscord)
	}

	fun removeBDOPatchChannel(serverID: String): String? {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		val patchChannelRemoveID = serverDiscord.bdoPatchIDChannel
		serverDiscord.bdoPatchIDChannel = null
		serverDiscordRepository.save(serverDiscord)
		return patchChannelRemoveID
	}

	fun getBDOPatchChannels(): ArrayList<ServerToChannel>{
		return serverDiscordRepository.getAllBDOPatchChannels()
	}

	/*fun getBDOPatchList(): ArrayList<String>{
		return jacksonObjectMapper().readValue(rssLinkRepository.getFirstById().lastPatchBDO)
	}*/

	fun getBDOPatchList(): String?{
		return rssLinkRepository.getFirstById().lastPatchBDO
	}

	fun setLastPatch(linkPatch:String){
		val rssLink = rssLinkRepository.getFirstById()
		rssLink.lastPatchBDO = linkPatch
		rssLinkRepository.save(rssLink)
	}


}