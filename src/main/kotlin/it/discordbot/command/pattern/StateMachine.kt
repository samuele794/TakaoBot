package it.discordbot.command.pattern

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent

interface StateMachine {
	/**
	 * Updates the state of this finite state machine.
	 *
	 * @param  event
	 * The event this state machine should use as input source
	 *
	 * @return True, if this state machine reached its final state
	 */
	fun updateStateMessage(event: GuildMessageReceivedEvent): Boolean

	fun updateStateReaction(event: GuildMessageReactionAddEvent): Boolean
}