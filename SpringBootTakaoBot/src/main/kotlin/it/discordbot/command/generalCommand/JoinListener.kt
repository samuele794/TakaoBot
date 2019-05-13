package it.discordbot.command.generalCommand

import it.discordbot.core.TakaoLog
import it.discordbot.database.interfaces.ServerManagementInterface
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Scope("singleton")
@Service
class JoinListener : ListenerAdapter() {

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	override fun onGuildJoin(event: GuildJoinEvent?) {
		serverManagementInterface.newServer(event!!.guild.id, event.guild.name)
		TakaoLog.logInfo("REGISTRAZIONE SERVER \nID = " + event.guild.id + "\nNOME = " + event.guild.name)
	}

	override fun onGuildLeave(event: GuildLeaveEvent?) {
		serverManagementInterface.deleteServer(event!!.guild.id)
	}

	override fun onGuildUpdateName(event: GuildUpdateNameEvent) {
		event.newName
	}
}