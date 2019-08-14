package it.discordbot.command.base

import it.discordbot.beans.RSSMessage
import it.discordbot.beans.ServerToChannel
import it.discordbot.command.BDO.RSS.BDONewsRSSScheduler
import it.discordbot.core.JDAController
import it.discordbot.core.TakaoLog
import net.dv8tion.jda.api.entities.MessageEmbed

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
	open fun publishMessage(message: MessageEmbed, serversToChannel: ArrayList<ServerToChannel>){
		for (obj in serversToChannel) {
			try {
				JDAController.jda.getGuildById(obj.serverID)
						?.getTextChannelById(obj.channelID)
						?.sendMessage(message)!!
						.queue()
			}catch (ex:Exception){
				TakaoLog.logError("Errore pubblicazione messaggi schedulazione RSS $ex")
			}

		}
	}
}