package it.discordbot.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

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
fun MessageReceivedEvent.checkCommand(symbolCommand: String, commandName: String): Boolean {

	val completeCommand = symbolCommand + commandName.toLowerCase()

	return if (!isFromType(ChannelType.PRIVATE)) {
		val commandEvent = if (message.isMentioned(this.jda.selfUser)) {
			message.contentRaw.split(" ")[1].toLowerCase()
		} else {
			message.contentRaw.split(" ")[0].toLowerCase()
		}
		commandEvent == completeCommand ||
				commandEvent.startsWith(commandName, true)
	} else {
		false
	}
}

/**
 * Metodo che controlla se l'utente ha i permessi di amministrare il server
 * @param this@checkAdminPermission MessageReceivedEvent evento del messaggio ricevuto
 * @return Boolean True, l'utente ha permessi amministrativi
 */
fun MessageReceivedEvent.checkAdminPermission(): Boolean {
	return guild.getMember(author)!!.hasPermission(Permission.ADMINISTRATOR)
}

/**
 * Metodo per inviare il messaggio prefatto che l'utente non ha i permessi necessari
 * @param event MessageReceivedEvent
 */
fun rejectCommand(event: MessageReceivedEvent) {
	event.textChannel.sendMessage(event.author.name + " non sei autorizzato all'uso di questo comando").queue()
}