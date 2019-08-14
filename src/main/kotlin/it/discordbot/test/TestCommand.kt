package it.discordbot.test

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component


@Component
class TestCommand : ListenerAdapter() {

	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.author.isBot) return

		if (event.message.contentRaw == "!test") {

		}
	}
}
