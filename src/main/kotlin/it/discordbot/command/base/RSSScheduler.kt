package it.discordbot.command.base

import it.discordbot.beans.RSSMessage
import it.discordbot.beans.ServerToChannel
import it.discordbot.core.JDAController
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException

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
	 * @throws InsufficientPermissionException permessi scrittura negati
	 */
	@Throws(InsufficientPermissionException::class)
	open fun publishMessage(message: MessageEmbed, serversToChannel: ArrayList<ServerToChannel>){
		for (obj in serversToChannel) {
			try {
				JDAController.jda.getGuildById(obj.serverID)
						?.getTextChannelById(obj.channelID)
						?.sendMessage(message)!!
						.queue()
			}catch (ex:Exception){
                JDAController.logger.error("Errore pubblicazione messaggi schedulazione RSS $ex")
			} catch (ex: InsufficientPermissionException){
				throw ex
			}

		}
	}
}