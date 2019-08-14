package it.discordbot.command.image

import it.discordbot.command.checkCommand
import it.discordbot.command.image.lorempicsum.LoremPicsumCommand
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

/**
 * Comandi relativi alle immagini
 * @property serverManagementInterface ServerManagementInterface
 * @property loremPicsumCommand LoremPicsumCommand
 */
@Scope("singleton")
@Service
class ImagesCommand : ListenerAdapter() {

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	@Autowired
	lateinit var loremPicsumCommand: LoremPicsumCommand

	companion object{
		const val RANDOM_PICSUM_COMMAND = "randomPicsum"

		const val RANDOM_PICSUM_COMMAND_DESCRIPTION = "Questo comando ti restituisce un immagine casuale da LoremPicsum. \n Il comando può essere lanciato su qualunque canale"

		const val CUSTOM_PICSUM_COMMAND = "customPicsum"

		const val CUSTOM_PICSUM_COMMAND_DESCRIPTION = "Questo comando ti permette di ottenere un immagine da LoremPicsum, in base a dei parametri. \n Il comando può essere lanciato su qualunque canale"

	}


	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.author.isBot)
			return

		val symbolCommand = serverManagementInterface.getSimbolCommand(event.guild.id)
		when {
			checkCommand(event, symbolCommand, RANDOM_PICSUM_COMMAND) -> {
				event.textChannel.sendMessage(loremPicsumCommand.getRandomPicsumImage()).queue()
			}
			checkCommand(event, symbolCommand, CUSTOM_PICSUM_COMMAND) -> {
				loremPicsumCommand.customPicsum(event)
			}
		}

	}

	override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
		if (event.author.isBot)
			return

		loremPicsumCommand.onGuildMessageReceived(event)
	}

	override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
		val author = event.member.user
		if (author.isBot)
			return

		loremPicsumCommand.onGuildMessageReactionAdd(event)
	}
}