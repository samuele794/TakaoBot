package it.discordbot

import it.discordbot.database.interfaces.BDONewsInterface
import net.dv8tion.jda.bot.entities.ApplicationInfo
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import net.dv8tion.jda.core.requests.RestAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TestCommand : ListenerAdapter() {

	@Autowired
	lateinit var bdoNewsInterface:BDONewsInterface

	override fun onMessageReceived(event: MessageReceivedEvent?) {
		if(event!!.author.isBot) return

		if (event.message.contentRaw == "!test") {

		}

	}
}