package it.discordbot.command.music

import it.discordbot.command.checkCommand
import it.discordbot.command.music.config.MusicManager
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
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

	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.author.isBot) return

		val symbolCommand = serverManagementInterface.getSymbolCommand(event.guild.id)

		if (event.checkCommand(symbolCommand, "leave")) {
			musicManager.leave(event)

		} else if (event.checkCommand(symbolCommand, "play")) {
			musicManager.play(event)

		} else if (event.checkCommand(symbolCommand, "playPlaylist")
				|| event.checkCommand(symbolCommand, "pp")) {
			musicManager.playPlaylist(event)

		} else if (event.checkCommand(symbolCommand, "skip") ||
				event.checkCommand(symbolCommand, "salta")) {
			musicManager.skip(event)

		} else if (event.checkCommand(symbolCommand, "pausa") ||
				event.checkCommand(symbolCommand, "pause") ||
				event.checkCommand(symbolCommand, "resume") ||
				event.checkCommand(symbolCommand, "riprendi")) {
			musicManager.pauseResume(event)

		} else if (event.checkCommand(symbolCommand, "stop")) {
			musicManager.stop(event)

		} else if (event.checkCommand(symbolCommand, "restart")) {
			musicManager.restart(event)

		} else if (event.checkCommand(symbolCommand, "repeat") ||
				event.checkCommand(symbolCommand, "ripeti")) {
			musicManager.repeat(event)

		} else if (event.checkCommand(symbolCommand, "nowplaying") ||
				event.checkCommand(symbolCommand, "np")) {
			musicManager.nowPlay(event)

		} else if (event.checkCommand(symbolCommand, "queue") ||
				event.checkCommand(symbolCommand, "np")) {
			musicManager.getListQueue(event)

		} else if (event.checkCommand(symbolCommand, "shuffle")) {
			musicManager.shuffleQueue(event)

		} else if (event.checkCommand(symbolCommand, "clear") ||
				event.checkCommand(symbolCommand, "pulisci")) {
			musicManager.clearQueue(event)

		}

		when {
			event.checkCommand(symbolCommand, "clear") ||
					event.checkCommand(symbolCommand, "pulisci") -> musicManager.clearQueue(event)
		}
	}
}