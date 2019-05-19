package it.discordbot.command.pattern

import it.discordbot.beans.RSSMessage
import net.dv8tion.jda.core.entities.MessageEmbed

/**
 * Interfaccia per la lettura e la conversione
 * dei messaggi RSS a MessageEmbed
 */
interface RSSReader {

	fun readRSS(url: String): RSSMessage

	fun prepareRSStoMessageEmbed(message: RSSMessage): MessageEmbed

}