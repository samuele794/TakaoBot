package it.discordbot.command.music

import it.discordbot.command.checkCommand
import it.discordbot.command.music.config.MusicManager
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Scope("singleton")
@Service
class MusicCommand : ListenerAdapter() {

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	@Autowired
	lateinit var musicManager: MusicManager

	override fun onMessageReceived(event: MessageReceivedEvent?) {
		if (event!!.author.isBot) return

		val symbolCommand = serverManagementInterface.getSimbolCommand(event.guild.id)

		if (checkCommand(event, symbolCommand, "leave")) {
			musicManager.leave(event)

		} else if (checkCommand(event, symbolCommand, "play")) {
			musicManager.play(event)

		} else if (checkCommand(event, symbolCommand, "playPlaylist")
				|| checkCommand(event, symbolCommand, "pp")) {
			musicManager.playPlaylist(event)

		} else if (checkCommand(event, symbolCommand, "skip") ||
				checkCommand(event, symbolCommand, "salta")) {
			musicManager.skip(event)

		} else if (checkCommand(event, symbolCommand, "pausa") ||
				checkCommand(event, symbolCommand, "pause") ||
				checkCommand(event, symbolCommand, "resume") ||
				checkCommand(event, symbolCommand, "riprendi")) {
			musicManager.pauseResume(event)

		} else if (checkCommand(event, symbolCommand, "stop")) {
			musicManager.stop(event)

		} else if (checkCommand(event, symbolCommand, "restart")) {
			musicManager.restart(event)

		} else if (checkCommand(event, symbolCommand, "repeat") ||
				checkCommand(event, symbolCommand, "ripeti")) {
			musicManager.repeat(event)

		} else if (checkCommand(event, symbolCommand, "nowplaying") ||
				checkCommand(event, symbolCommand, "np")) {
			musicManager.nowPlay(event)

		} else if (checkCommand(event, symbolCommand, "queue") ||
				checkCommand(event, symbolCommand, "np")) {
			musicManager.getListQueue(event)

		} else if (checkCommand(event, symbolCommand, "shuffle")) {
			musicManager.shuffleQueue(event)

		} else if (checkCommand(event, symbolCommand, "clear") ||
				checkCommand(event, symbolCommand, "pulisci")) {
			musicManager.clearQueue(event)

		}

		when{
			checkCommand(event, symbolCommand, "clear") ||
			checkCommand(event, symbolCommand, "pulisci") -> musicManager.clearQueue(event)
		}
	}
}