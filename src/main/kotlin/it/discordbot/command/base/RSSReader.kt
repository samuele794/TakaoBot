package it.discordbot.command.base

import it.discordbot.beans.RSSMessage
import net.dv8tion.jda.api.entities.MessageEmbed

/**
 * Interfaccia per la lettura e la conversione
 * dei messaggi RSS a MessageEmbed
 */
interface RSSReader {
	/**
	 * Metodo per leggere il feed RSS da un url RSS
	 * @param url String url del feed
	 * @return RSSMessage
	 */
	fun readRSS(url: String): RSSMessage

	/**
	 * Metodo per convertire il messagggio RSS in un MessageEmbed
	 * @param message RSSMessage
	 * @return MessageEmbed
	 */
	fun prepareRSStoMessageEmbed(message: RSSMessage): MessageEmbed

}