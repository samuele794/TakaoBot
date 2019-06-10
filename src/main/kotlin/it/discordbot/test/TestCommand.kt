package it.discordbot.test

import it.discordbot.command.image.lorempicsum.LoremPicsumRetreiver
import it.discordbot.core.JDAController
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit


@Component
class TestCommand : ListenerAdapter() {

	@Autowired
	private lateinit var picsumClient: LoremPicsumRetreiver


	override fun onMessageReceived(event: MessageReceivedEvent?) {
		if (event!!.author.isBot) return

		if (event.message.contentRaw == "!test") {
//			event.textChannel.sendMessage(picsumClient.getImagePicsum()).queue()
			JDAController.eventWaiter.waitForEvent(MessageReceivedEvent::class.java,
					// make sure it's by the same user, and in the same channel
					{ e -> e.member.user == event.author && e.channel == event.channel },
					// respond, inserting the name they listed into the response
					{ e ->
						e.textChannel.sendMessage("Test1").queue()
						JDAController.eventWaiter.waitForEvent(MessageReceivedEvent::class.java,
								// make sure it's by the same user, and in the same channel
								{ e -> e.member.user == event.author && e.channel == event.channel },
								// respond, inserting the name they listed into the response
								{ e -> e.textChannel.sendMessage("Test2").queue() },
								// if the user takes more than a minute, time out
								30, TimeUnit.SECONDS, { event.textChannel.sendMessage("Sorry, you took too long.").queue() })

					},
					// if the user takes more than a minute, time out
					30, TimeUnit.SECONDS, { event.textChannel.sendMessage("Sorry, you took too long.").queue() })

		}

	}
}
