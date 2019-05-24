package it.discordbot.database.filter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import it.discordbot.beans.ServerToChannel
import it.discordbot.beans.bdo.boss.Giorno
import it.discordbot.database.repository.ServerDiscordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Controller

/**
 * Classe di interfacciamento al db per le funzioni di BDO Boss
 * @property serverDiscordRepository ServerDiscordRepository
 */
@Scope("singleton")
@Controller
class BDOBossInterface {

	@Autowired
	lateinit var serverDiscordRepository: ServerDiscordRepository

	fun setBDOBossChannel(serverID: String, channelID: String) {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		serverDiscord.bdoBossIDChannel = channelID
		serverDiscordRepository.save(serverDiscord)
	}

	fun removeBDOBossChannel(serverID: String): String? {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		val bossChannelRemoveID = serverDiscord.bdoBossIDChannel
		serverDiscord.bdoBossIDChannel = null
		serverDiscordRepository.save(serverDiscord)
		return bossChannelRemoveID
	}

	fun getBDOBossChannels(): ArrayList<ServerToChannel> {
		return serverDiscordRepository.getAllBDOBossChannels()
	}

	fun getBDOBossList(): ArrayList<Giorno> {
		val inputStreamBoss = ClassPathResource("jsonboss.json").inputStream
		val list = jacksonObjectMapper().readValue<ArrayList<Giorno>>(inputStreamBoss)
		inputStreamBoss.close()
		return list
	}
}