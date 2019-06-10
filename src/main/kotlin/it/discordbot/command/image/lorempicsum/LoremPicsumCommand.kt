package it.discordbot.command.image.lorempicsum

import it.discordbot.beans.image.ImageCommandProxy
import it.discordbot.command.pattern.StateMachine
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.*

@Scope("singleton")
@Component
class LoremPicsumCommand {


	@Autowired
	lateinit var imageCommandProxy: ImageCommandProxy

	private val stateMachines = HashMap<String, StateMachine>()

	@Autowired
	lateinit var loremPicsumRetreiver: LoremPicsumRetreiver

	fun getRandomPicsumImage(): String {
		return loremPicsumRetreiver.getImagePicsum()
	}

	fun customPicsum(event: MessageReceivedEvent) {
		stateMachines[event.author.id] = imageCommandProxy.getLoremPicsumMachine().apply {
			channel = event.textChannel
			authorID = event.author.id
			startMachina()
		}
	}

	fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
		val author = event.author
		val stateMachine = stateMachines[event.author.id]
		if (stateMachine != null && stateMachine.updateStateMessage(event)) {
			stateMachines.remove(author.id)
		}
	}

	fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
		val author = event.member.user
		val stateMachine = stateMachines[author.id]
		if (stateMachine != null && stateMachine.updateStateReaction(event)) {
			stateMachines.remove(author.id)
		}
	}

}