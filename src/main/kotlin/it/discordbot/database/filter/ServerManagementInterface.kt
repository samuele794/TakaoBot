package it.discordbot.database.filter

import it.discordbot.beans.ServerDiscord
import it.discordbot.database.repository.ServerDiscordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Classe di interfacciamento al db per le funzioni base del BOT
 * @property serverDiscordRepository ServerDiscordRepository
 */
@Scope("singleton")
@Component
class ServerManagementInterface {

	@Autowired
	private lateinit var serverDiscordRepository: ServerDiscordRepository

	@Cacheable("SymbolCommandCache", key = "#serverID")
	fun getSymbolCommand(serverID: String): String {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		return serverDiscord.simbolCommand!!
	}

	@CachePut("SymbolCommandCache", key = "#serverID")
	fun setSymbolCommand(serverID: String, simbolCommand: String): String {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		serverDiscord.simbolCommand = simbolCommand
		serverDiscordRepository.save(serverDiscord)
		return simbolCommand
	}

	fun newServer(serverID: String, serverName: String) {
		val serverDiscord = ServerDiscord()
		serverDiscord.let {
			it.serverId = serverID
			it.nameServer = serverName
		}
		serverDiscordRepository.save(serverDiscord)
	}

	fun deleteServer(serverID: String) {
		serverDiscordRepository.deleteById(serverID)
	}
}