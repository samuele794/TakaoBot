package it.discordbot.database.filter

import it.discordbot.beans.ServerDiscord
import it.discordbot.database.repository.ServerDiscordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Scope("singleton")
@Component
class ServerManagementInterface {

	@Autowired
	private lateinit var serverDiscordRepository: ServerDiscordRepository


	fun getSimbolCommand(serverID: String): String {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		return serverDiscord.simbolCommand!!
	}

	fun setSimbolCommand(serverID: String, simbolCommand: String) {
		val serverDiscord = serverDiscordRepository.findServerDiscordByServerId(serverID)
		serverDiscord.simbolCommand = simbolCommand
		serverDiscordRepository.save(serverDiscord)
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