package it.discordbot.command.pattern

import it.discordbot.beans.RSSMessage
import it.discordbot.beans.ServerToChannel
import net.dv8tion.jda.core.entities.MessageEmbed

interface RSSScheduler {

	fun procedurePublish(rssMessage: RSSMessage)

	fun publishMessage(message: MessageEmbed, serversToChannel: ArrayList<ServerToChannel>)
}