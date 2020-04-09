package it.discordbot.command.BDO

import it.discordbot.command.checkAdminPermission
import it.discordbot.command.checkCommand
import it.discordbot.command.rejectCommand
import it.discordbot.database.filter.BDOBossInterface
import it.discordbot.database.filter.BDONewsInterface
import it.discordbot.database.filter.BDOPatchInterface
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.awt.Color

/**
 * Listener per contenere i comandi di BDO
 * @property serverManagementInterface ServerManagementInterface
 * @property bdoNewsInterface BDONewsInterface
 * @property bdoPatchInterface BDOPatchInterface
 * @property bdoBossInterface BDOBossInterface
 */
@Scope("singleton")
@Service
class BDOCommand : ListenerAdapter() {

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	@Autowired
	lateinit var bdoNewsInterface: BDONewsInterface

	@Autowired
	lateinit var bdoPatchInterface: BDOPatchInterface

	@Autowired
	lateinit var bdoBossInterface: BDOBossInterface

	companion object {

		const val BDO_BOSS_START_COMMAND = "BDOBossStart"

		const val BDO_BOSS_START_COMMAND_DESCRIPTION = "Questo comando permette di iscriversi agli allarmi dei boss di BDO. \n Il comando deve essere lanciato sul canale su cui si desidera ricevere i boss"

		const val BDO_BOSS_STOP_COMMAND = "BDOBossStop"

		const val BDO_BOSS_STOP_COMMAND_DESCRIPTION = "Questo comando permette disiscriversi agli allarmi dei boss di BDO. \n Il comando può essere lanciato su qualunque canale"

		const val BDO_BOSS_TABLE = "BDOBossTable"

		const val BDO_BOSS_TABLE_COMMAND_DESCRIPTION = "Questo comando ti fa vedere la tabella dei boss di BDO."


		const val BDO_NEWS_START_COMMAND = "BDONewsStart"

		const val BDO_NEWS_START_COMMAND_DESCRIPTION = "Questo comando permette di iscriversi al feed delle news di BDO. \n Il comando deve essere lanciato sul canale su cui si desidera ricevere le news"

		const val BDO_NEWS_STOP_COMMAND = "BDONewsStop"

		const val BDO_NEWS_STOP_COMMAND_DESCRIPTION = "Questo comando permette disiscriversi al feed delle news di BDO. \n Il comando può essere lanciato su qualunque canale"

		const val BDO_PATCH_START_COMMAND = "BDOPatchStart"

		const val BDO_PATCH_START_COMMAND_DESCRIPTION = "Questo comando permette di iscriversi al feed delle patch di BDO. \n" + "Il comando deve essere lanciato sul canale su cui si desidera ricevere le patch"

		const val BDO_PATCH_STOP_COMMAND = "BDOPatchStop"

		const val BDO_PATCH_STOP_COMMAND_DESCRIPTION = "Comando per disiscriversi al feed delle patch di BDO. \n" + "Il comando può essere lanciato su qualunque canale"

	}

	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.author.isBot) return

		val symbolCommand = serverManagementInterface.getSymbolCommand(event.guild.id)

		when {
			event.checkCommand(symbolCommand, BDO_BOSS_START_COMMAND) -> {
				if (checkAdminPermission(event)) {
					bdoBossInterface.setBDOBossChannel(event.guild.id, event.textChannel.id)
					sendMessageAddChannel(event, "Invio degli allarmi dei boss di BDO configurato sul canale: ")
				} else {
					rejectCommand(event)
				}
			}

			event.checkCommand(symbolCommand, BDO_BOSS_STOP_COMMAND) -> {
				if (!checkAdminPermission(event)) {
					rejectCommand(event)
				} else {
					val removedChannelId = bdoBossInterface.removeBDOBossChannel(event.guild.id)
					if (removedChannelId != null)
						sendMessageRemoveChannel(event, "Invio degli allarmi dei boss di BDO rimosso dal canale: ", removedChannelId)
				}
			}

			event.checkCommand(symbolCommand, BDO_BOSS_TABLE) -> {
				event.textChannel.sendMessage(EmbedBuilder().apply {
					setImage("https://i.imgur.com/0JdziL3.png")
					setColor(Color(40, 40, 40))
				}.build()).queue()
			}

			event.checkCommand(symbolCommand, BDO_NEWS_START_COMMAND) -> {
				if (!checkAdminPermission(event)) {
					rejectCommand(event)
				} else {
					bdoNewsInterface.setBDONewsChannel(event.guild.id, event.textChannel.id)
					sendMessageAddChannel(event, "Invio delle news di BDO configurato sul canale: ")
				}
			}

			event.checkCommand(symbolCommand, BDO_NEWS_STOP_COMMAND) -> {
				if (!checkAdminPermission(event)) {
					rejectCommand(event)
				} else {
					val removedChannelId = bdoNewsInterface.removeBDONewsChannel(event.guild.id)
					if (removedChannelId != null)
						sendMessageRemoveChannel(event, "Invio delle news di BDO rimosso dal canale: ", removedChannelId)
				}
			}

			event.checkCommand(symbolCommand, BDO_PATCH_START_COMMAND) -> {
				if (!checkAdminPermission(event)) {
					rejectCommand(event)
				} else {
					bdoPatchInterface.setBDOPatchChannel(event.guild.id, event.textChannel.id)
					sendMessageAddChannel(event, "Invio delle patch di BDO configurato sul canale: ")
				}
			}

			event.checkCommand(symbolCommand, BDO_PATCH_STOP_COMMAND) -> {
				if (!checkAdminPermission(event)) {
					rejectCommand(event)
				} else {
					val removedChannelId = bdoPatchInterface.removeBDOPatchChannel(event.guild.id)
					if (removedChannelId != null)
						sendMessageRemoveChannel(event, "Invio delle patch di BDO rimosso dal canale: ", removedChannelId)
				}
			}
		}

	}

	private fun sendMessageAddChannel(event: MessageReceivedEvent, message: String) {
		MessageBuilder().apply {
			append(message)
			appendCodeBlock(event.textChannel.name, "")
		}.sendTo(event.textChannel).queue()
	}

	private fun sendMessageRemoveChannel(event: MessageReceivedEvent, message: String, channelID: String) {
		MessageBuilder().apply {
			append(message)
			appendCodeBlock(event.jda.getTextChannelById(channelID)!!.name, "")
		}.sendTo(event.textChannel).queue()
	}


}