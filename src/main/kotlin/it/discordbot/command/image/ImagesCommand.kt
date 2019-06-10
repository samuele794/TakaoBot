package it.discordbot.command.image

import it.discordbot.command.checkCommand
import it.discordbot.command.image.lorempicsum.LoremPicsumCommand
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Scope("singleton")
@Service
class ImagesCommand : ListenerAdapter() {

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	@Autowired
	lateinit var loremPicsumCommand: LoremPicsumCommand


	override fun onMessageReceived(event: MessageReceivedEvent?) {
		if (event!!.author.isBot)
			return

		val symbolCommand = serverManagementInterface.getSimbolCommand(event.guild.id)
		when {
			checkCommand(event, symbolCommand, "randomPicsum") -> {
				event.textChannel.sendMessage(loremPicsumCommand.getRandomPicsumImage()).queue()
			}
			checkCommand(event, symbolCommand, "customPicsum") -> {
				loremPicsumCommand.customPicsum(event)
			}
		}

	}

	override fun onGuildMessageReceived(event: GuildMessageReceivedEvent?) {
		if (event!!.author.isBot)
			return

		loremPicsumCommand.onGuildMessageReceived(event)
	}

	override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent?) {
		val author = event!!.member.user
		if (author.isBot)
			return

		loremPicsumCommand.onGuildMessageReactionAdd(event)
	}
}