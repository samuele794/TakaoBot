package it.discordbot.command.generalCommand

import it.discordbot.core.TakaoLog
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

/**
 * Listener per contenere quando il bot entra o esce
 * da un server Discord
 * @property serverManagementInterface ServerManagementInterface
 */
@Scope("singleton")
@Service
class JoinListener : ListenerAdapter() {

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	override fun onGuildJoin(event: GuildJoinEvent) {
		serverManagementInterface.newServer(event.guild.id, event.guild.name)
		TakaoLog.logInfo("REGISTRAZIONE SERVER \nID = " + event.guild.id + "\nNOME = " + event.guild.name)
	}

	override fun onGuildLeave(event: GuildLeaveEvent) {
		serverManagementInterface.deleteServer(event.guild.id)
		TakaoLog.logInfo("REGISTRAZIONE SERVER RIMOSSA \nID = " + event.guild.id + "\nNOME = " + event.guild.name)

	}

	override fun onGuildUpdateName(event: GuildUpdateNameEvent) {
		event.newName
	}

	/*override fun onGuildMemberNickChange(event: GuildMemberNickChangeEvent?) {
//		bloccare cambio nickname bot
	}*/
}