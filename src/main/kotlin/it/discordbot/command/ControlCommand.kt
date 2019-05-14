package it.discordbot.command

import it.discordbot.database.interfaces.ServerManagementInterface
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.jetbrains.annotations.NotNull

fun checkCommand(@NotNull event: MessageReceivedEvent,
				 @NotNull simbolCommand: String,
				 @NotNull commandName: String): Boolean {

	val completeCommand = simbolCommand + commandName.toLowerCase()

	return if (!event.isFromType(ChannelType.PRIVATE)) {
		if (event.message.contentRaw == completeCommand ||
				event.message.isMentioned(event.jda.selfUser) &&
				event.message.contentRaw.contains(commandName)) {
			true
		} else {
			false
		}
	} else {
		false
	}
}

fun checkAdminPermission(event: MessageReceivedEvent): Boolean {
	return event.guild.getMember(event.author).hasPermission(Permission.ADMINISTRATOR)
}

fun rejectCommand(event: MessageReceivedEvent) {
	event.textChannel.sendMessage(event.author.name + " non sei autorizzato all'uso di questo comando").queue()
}