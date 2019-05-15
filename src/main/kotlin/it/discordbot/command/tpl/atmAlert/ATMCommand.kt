package it.discordbot.command.tpl.atmAlert

import it.discordbot.command.checkAdminPermission
import it.discordbot.command.checkCommand
import it.discordbot.command.rejectCommand
import it.discordbot.database.filter.ATMInterface
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Scope("singleton")
@Service
class ATMCommand : ListenerAdapter() {

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	@Autowired
	lateinit var atmInterface: ATMInterface

	companion object {
		const val ATM_START_COMMAND = "ATMAlertStart"

		const val ATM_START_COMMAND_DESCRIPTION = "Questo comando permette di iscriversi agli avvisi dell'ATM Milano. \n" + "Il comando deve essere lanciato sul canale su cui si desidera ricevere gli avvisi"

		const val ATM_STOP_COMMAND = "ATMAlertStop"

		const val ATM_STOP_COMMAND_DESCRIPTION = "Questo comando permette disiscriversi agli avvisi dell'ATM Milano. \n" + "Il comando può essere lanciato su qualunque canale"

	}

	override fun onMessageReceived(event: MessageReceivedEvent?) {
		if (event!!.author.isBot) return

		val symbolCommand = serverManagementInterface.getSimbolCommand(event.guild.id)

		if (checkCommand(event, symbolCommand, ATM_START_COMMAND)) {
			if (!checkAdminPermission(event)) {
				rejectCommand(event)
			} else {
				atmInterface.setATMAlertChannel(event.guild.id, event.textChannel.id)
				MessageBuilder().apply {
					append("Invio degli avvisi dell'ATM configurato sul canale: ")
					appendCodeBlock(event.textChannel.name, "")
				}.sendTo(event.textChannel).queue()
			}
		} else if (checkCommand(event, symbolCommand, ATM_STOP_COMMAND)) {
			if (!checkAdminPermission(event)) {
				rejectCommand(event)
			} else {
				val removedChannelID = atmInterface.removeATMAlertChannel(event.guild.id)
				MessageBuilder().apply {
					append("Invio degli avvisi dell'ATM rimosso dal canale: ")
					appendCodeBlock(event.jda.getTextChannelById(removedChannelID).name, "")
				}.sendTo(event.channel).queue()
			}

		}

	}

}