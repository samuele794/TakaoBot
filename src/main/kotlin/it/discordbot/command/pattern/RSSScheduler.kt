package it.discordbot.command.pattern

import it.discordbot.beans.RSSMessage
import it.discordbot.beans.ServerToChannel
import net.dv8tion.jda.core.entities.MessageEmbed

/**
 * Interfaccia base per i metodi di pubblicazione dei messaggi RSS
 */
interface RSSScheduler {
	/**
	 * Metodo per la preparazione alla pubblicazione dei messaggi RSS
	 * @param rssMessage RSSMessage messaggio RSS
	 */
	fun procedurePublish(rssMessage: RSSMessage)

	/**
	 * Metodo per la pubblicazione dei messaggi
	 * @param message MessageEmbed Messaggio
	 * @param serversToChannel ArrayList<ServerToChannel> Lista server e canali
	 */
	fun publishMessage(message: MessageEmbed, serversToChannel: ArrayList<ServerToChannel>)
}