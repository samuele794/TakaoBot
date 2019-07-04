package it.discordbot.command

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.jetbrains.annotations.NotNull

/**
 * Metodo per controllare se il comando inserito dall'utente
 * è il comando controllato.
 * Viene controllato se il messaggio è con il simbolo di comando
 * o viene citato il bot.
 * @param event MessageReceivedEvent evento del messaggio ricevuto
 * @param simbolCommand String simbolo di comando del server
 * @param commandName String nome del comando
 * @return Boolean il comando confrontato e quello voluto sono uguali
 */
fun checkCommand(@NotNull event: MessageReceivedEvent,
				 @NotNull simbolCommand: String,
				 @NotNull commandName: String): Boolean {

	val completeCommand = simbolCommand + commandName.toLowerCase()
	val commandEvent = event.message.contentRaw.split(" ")[0].toLowerCase()

	return if (!event.isFromType(ChannelType.PRIVATE)) {
		if (commandEvent == completeCommand ||
				event.message.isMentioned(event.jda.selfUser) &&
				event.message.contentRaw.contains(commandName, true)) {
			true
		} else {
			false
		}
	} else {
		false
	}
}

/**
 * Metodo che controlla se l'utente ha i permessi di amministrare il server
 * @param event MessageReceivedEvent evento del messaggio ricevuto
 * @return Boolean True, l'utente ha permessi amministrativi
 */
fun checkAdminPermission(event: MessageReceivedEvent): Boolean {
	return event.guild.getMember(event.author).hasPermission(Permission.ADMINISTRATOR)
}

/**
 * Metodo per inviare il messaggio prefatto che l'utente non ha i permessi necessari
 * @param event MessageReceivedEvent
 */
fun rejectCommand(event: MessageReceivedEvent) {
	event.textChannel.sendMessage(event.author.name + " non sei autorizzato all'uso di questo comando").queue()
}