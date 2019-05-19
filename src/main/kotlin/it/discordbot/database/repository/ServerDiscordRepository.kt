package it.discordbot.database.repository

import it.discordbot.beans.ServerDiscord
import it.discordbot.beans.ServerToChannel
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ServerDiscordRepository : CrudRepository<ServerDiscord, String> {

	fun findServerDiscordByServerId(serverID: String): ServerDiscord

	@Query("SELECT new it.discordbot.beans.ServerToChannel(serv.serverId, serv.bdoNewsIDChannel) " +
			"from ServerDiscord serv where serv.bdoNewsIDChannel is not null")
	fun getAllBDONewsChannels(): ArrayList<ServerToChannel>


	@Query("SELECT new it.discordbot.beans.ServerToChannel(serv.serverId, serv.bdoPatchIDChannel) " +
			"from ServerDiscord serv where serv.bdoPatchIDChannel is not null")
	fun getAllBDOPatchChannels(): ArrayList<ServerToChannel>

	@Query("SELECT new it.discordbot.beans.ServerToChannel(serv.serverId, serv.bdoBossIDChannel) " +
			"from ServerDiscord serv where serv.bdoBossIDChannel is not null")
	fun getAllBDOBossChannels(): ArrayList<ServerToChannel>

	@Query("SELECT new it.discordbot.beans.ServerToChannel(serv.serverId, serv.atmAlertIDChannel) " +
			"from ServerDiscord serv where serv.atmAlertIDChannel is not null")
	fun getAllATMAlertChannels(): ArrayList<ServerToChannel>
}
