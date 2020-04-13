package it.discordbot.command.generalCommand

import it.discordbot.command.BDO.BDOCommand
import it.discordbot.command.checkAdminPermission
import it.discordbot.command.checkCommand
import it.discordbot.command.image.ImagesCommand
import it.discordbot.command.rejectCommand
import it.discordbot.command.tpl.atm.ATMCommand
import it.discordbot.core.JDAController
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.awt.Color
import java.util.*

/**
 * Listener per contenere comandi di uso generale e di configurazione del bot
 * @property serverManagementInterface ServerManagementInterface
 */
@Scope(value = "singleton")
@Service
class GeneralCommand : ListenerAdapter() {

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	companion object {

		const val CONFIGURATION_COMMAND = "configurationCommand"

		const val CONFIGURATION_COMMAND_DESCRIPTION = "Questo comando permette la configurazione del prefisso per i comandi \n" + "Questo comando è riservato al solo utilizzo da parte del proprietario del server."

		const val HELP_COMMAND = "help"

		const val HELP_COMMAND2 = "aiuto"

		const val INFO_COMMAND = "info"

		const val INFO_COMMAND_DESCRIPTION = "Info del bot."


	}

	override fun onMessageReceived(event: MessageReceivedEvent) {

		if (event.author.isBot) return
		if (event.isFromType(ChannelType.PRIVATE)) return

		val symbolCommand = serverManagementInterface.getSymbolCommand(event.guild.id)
		when {
			event.checkCommand(symbolCommand, CONFIGURATION_COMMAND) -> {
				getConfigurationCommand(event)
			}

			event.checkCommand(symbolCommand, INFO_COMMAND) -> {
				event.author.openPrivateChannel().queue {
					it.sendMessage(getInfo()).queue()
				}
			}

			event.checkCommand(symbolCommand, HELP_COMMAND) ||
					event.checkCommand(symbolCommand, HELP_COMMAND2) -> {
				event.author.openPrivateChannel().queue {
					it.sendMessage(getHelp(event.guild.id)).queue()
				}
			}
		}

	}

	private fun getHelp(serverID: String): MessageEmbed {

		val symbolCommand = serverManagementInterface.getSymbolCommand(serverID)

		return EmbedBuilder().apply {
			setTitle("Lista Comandi")
			setColor(Color(130, 195, 250))
			setDescription("Questi comandi possono essere usati menzionando il bot e scrivendo il comando senza simbolo,"
					+ "oppure usando i comandi direttamente come segue:")
			addField(symbolCommand + INFO_COMMAND, INFO_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + CONFIGURATION_COMMAND, CONFIGURATION_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + BDOCommand.BDO_NEWS_START_COMMAND, BDOCommand.BDO_NEWS_START_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + BDOCommand.BDO_NEWS_STOP_COMMAND, BDOCommand.BDO_NEWS_STOP_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + BDOCommand.BDO_PATCH_START_COMMAND, BDOCommand.BDO_PATCH_START_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + BDOCommand.BDO_PATCH_STOP_COMMAND, BDOCommand.BDO_PATCH_STOP_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + BDOCommand.BDO_BOSS_START_COMMAND, BDOCommand.BDO_BOSS_START_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + BDOCommand.BDO_BOSS_STOP_COMMAND, BDOCommand.BDO_BOSS_STOP_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + BDOCommand.BDO_BOSS_TABLE, BDOCommand.BDO_BOSS_TABLE_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + ATMCommand.ATM_START_COMMAND, ATMCommand.ATM_START_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + ATMCommand.ATM_STOP_COMMAND, ATMCommand.ATM_STOP_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + ATMCommand.ATM_METRO_STATUS_COMMAND + "   " + symbolCommand + ATMCommand.ATM_METRO_STATUS2_COMMAND, ATMCommand.ATM_METRO_STATUS_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + ImagesCommand.RANDOM_PICSUM_COMMAND, ImagesCommand.RANDOM_PICSUM_COMMAND_DESCRIPTION, false)
			addField(symbolCommand + ImagesCommand.CUSTOM_PICSUM_COMMAND, ImagesCommand.CUSTOM_PICSUM_COMMAND_DESCRIPTION, false)
		}.build()
	}

	private fun getInfo(): MessageEmbed {

		val info = "Ciao, sono samuele794#8585 lo sviluppatore di questo bot, scritto Java 8 con l'utilizzo " +
				"della libreria di base JDA (Java Discord Api). \n\n" +
				"Il personaggio Takao di Arpeggio Of Blue Steel (\u84bc\u304d\u92fc\u306e\u30a2\u30eb\u30da\u30b8\u30aa, Aoki Hagane no Arpeggio) " +
				"è stato creato da Ark Performance. \nNon sono il possessore di nessuna immagine " +
				"utilizzata all'interno del bot. \n\n" +
				"Se sei uno sviluppatore Java e vuoi contribuire al bot sei ben accetto. \n" +
				"Link GitHub: https://github.com/samuele794/TakaoBot \n\n" +
				"Usando questo bot accetti il fatto che conserverò l'uso dei comandi per fini della manutenzione del bot \n\n" +
				"Link utili: \n" +
				"- Sito web: https://samuele794.github.io/TakaoBot/"

		val avatarUrl = JDAController.jda.getUserById("186582756841488385")!!.avatarUrl

		return EmbedBuilder().apply {
			setImage("https://samuele794.github.io/TakaoBot/images/Copertina.png")
			setThumbnail(avatarUrl)
			setDescription(info)
			setColor(Color(131, 196, 250))
		}.build()
	}

	//configurationCommand
	private fun getConfigurationCommand(event: MessageReceivedEvent) {
		if (!event.checkAdminPermission()) {
			rejectCommand(event)
		} else {
			val tokenizer = StringTokenizer(event.message.contentRaw)
			if (event.message.isMentioned(event.jda.selfUser)) {
				if (tokenizer.countTokens() == 3) {
					configureSymbol(event)
				} else {
					event.textChannel.sendMessage("Quantità di parametri non conformi").queue()
				}
			} else {
				if (tokenizer.countTokens() == 2) {
					configureSymbol(event)
				} else {
					event.textChannel.sendMessage("Quantità di parametri non conformi").queue()
				}
			}
		}
	}

	private fun configureSymbol(event: MessageReceivedEvent) {
		if (!(event.message.contentRaw.contains("\"") or event.message.contentRaw.contains("\\") or
						event.message.contentRaw.contains("'"))) {

			val listMessage = event.message.contentRaw.split(" ")
			val newCommand = listMessage[listMessage.size - 1]
			serverManagementInterface.setSymbolCommand(event.guild.id, newCommand)

			MessageBuilder().append("Simbolo di comando configurato. Nuovo simbolo di comando: ")
					.appendCodeBlock(serverManagementInterface.getSymbolCommand(event.guild.id), "").sendTo(event.textChannel).queue()
		} else {
			MessageBuilder().append("Simbolo di comando non conforme").sendTo(event.textChannel).queue()
		}
	}

}