package it.discordbot.beans.image

import it.discordbot.command.image.lorempicsum.LoremPicsumRetreiver
import it.discordbot.command.pattern.StateMachine
import it.discordbot.core.JDAController
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Scope("prototype")
@Component
class LoremPicsumStateMachine : StateMachine {

	@Autowired
	lateinit var loremPicsumRetreiver: LoremPicsumRetreiver

	lateinit var channel: TextChannel
	lateinit var authorID: String

	private var width: Int = 1920
	private var height: Int = 1080
	private var grayScale = false
	private var blur: Int = 0

	private val YES = "\u2705"
	private val NO = "\u274C"

	private var messageID: String = ""
	private var statoMachina = 0

	private var tentativi = 0

	private var fase4 = false

	override fun updateStateMessage(event: GuildMessageReceivedEvent): Boolean {
		return updateState()
	}

	override fun updateStateReaction(event: GuildMessageReactionAddEvent): Boolean {
		return stateMachina(eventGuiltReaction = event)
	}

	private fun updateState(): Boolean {
		return stateMachina(null)
	}

	fun startMachina() {
		channel.sendMessage("Impostazioni Ricerca Lorem Picsum").queue {
			it.delete().queueAfter(2, TimeUnit.MINUTES)
		}
		customDimensionMessageSend()
		JDAController.eventWaiter.waitForEvent(MessageReactionAddEvent::class.java,
				// make sure it's by the same user, and in the same channel
				{ e -> e.member.user.id == authorID && e.textChannel == channel && e.messageId == messageID },
				// respond, inserting the name they listed into the response
				{ e ->
					when {
						e.reactionEmote.name == YES -> {
							statoMachina = 1
							customDimensionFormMessageSend()
							updateState()
						}
						e.reactionEmote.name == NO -> {
							statoMachina = 2
							grayScaleMessageSend()
							updateState()
						}
					}
				},
				30, TimeUnit.SECONDS, {
			timeoutMessage()
		})
	}


	private fun stateMachina(eventGuiltReaction: GuildMessageReactionAddEvent?): Boolean {
		when (statoMachina) {

			1 -> {
				if (tentativi < 3) {
					//attesa risposta per dimensioni immagine
					JDAController.eventWaiter.waitForEvent(MessageReceivedEvent::class.java,
							{ e ->
								e.author.id == authorID
										&&
										e.channel.id == channel.id
							},
							{ e ->
								val params: List<String>
								try {
									params = e.message.contentRaw.split("x")
									width = params[0].toInt()
									height = params[1].toInt()
									statoMachina = 2
									grayScaleMessageSend()
								} catch (e: Exception) {
									channel.sendMessage("Parametri non conformi").queue {
										it.delete().queueAfter(5, TimeUnit.SECONDS)
									}
									tentativi++
								}

							},
							// if the user takes more than a minute, time out
							30, TimeUnit.SECONDS, {
						timeoutMessage()
					})
				} else {
					channel.sendMessage("Tentativi Esauriti, procedura annullata").queue {
						it.delete().queueAfter(10, TimeUnit.SECONDS)
					}
					statoMachina = 999
				}

			}

			2 -> {
				//attesa risposta scala grigi
				JDAController.eventWaiter.waitForEvent(MessageReactionAddEvent::class.java,
						{ e -> e.member.user == eventGuiltReaction?.user && e.textChannel == eventGuiltReaction?.channel && e.messageId == messageID },

						{ e ->
							when {
								e.reactionEmote.name == YES -> {
									statoMachina = 3
									grayScale = true
									blurMessageSend()
									fase4 = true
								}
								e.reactionEmote.name == NO -> {
									statoMachina = 3
									grayScale = false
									blurMessageSend()
									fase4 = true
								}
							}
						},
						// if the user takes more than a minute, time out
						1, TimeUnit.MINUTES, {
					if (!fase4) {
						timeoutMessage()
					}

				})
			}

			3 -> {
				//attesa risposta sfocatura
				JDAController.eventWaiter.waitForEvent(MessageReactionAddEvent::class.java,
						{ e -> e.member.user == eventGuiltReaction?.user && e.textChannel == eventGuiltReaction?.channel && e.messageId == messageID },
						{ e ->
							when {
								e.reactionEmote.name == YES -> {
									statoMachina = 4
									blurFormMessageSend()
								}
								e.reactionEmote.name == NO -> {
									statoMachina = 999
									executeCommand()
								}
							}
						},
						// if the user takes more than a minute, time out
						30, TimeUnit.SECONDS, {
					timeoutMessage()
				})
			}

			4 -> {
				//attesa risposta parametri sfocatura
				JDAController.eventWaiter.waitForEvent(MessageReceivedEvent::class.java,
						{ e ->
							e.author.id == authorID
									&&
									e.channel.id == channel.id
						},
						{ e ->
							if (tentativi < 3) {
								try {
									blur = e.message.contentRaw.toInt()
									statoMachina = 999
									executeCommand()

								} catch (e: Exception) {
									channel.sendMessage("Parametri non conformi").queue {
										it.delete().queueAfter(5, TimeUnit.SECONDS)
									}
									tentativi++
								}
							} else {
								channel.sendMessage("Tentativi Esauriti, procedura annullata 7").queue {
									it.delete().queueAfter(5, TimeUnit.SECONDS)
								}
								statoMachina = 999
							}
						},
						30, TimeUnit.SECONDS, {
					timeoutMessage()
				})
			}

			999 -> return true

		}

		return false
	}

	private fun customDimensionMessageSend() {
		channel.sendMessage("Vuoi cambiare le dimensioni di default? " +
				"${LoremPicsumRetreiver.baseWidth}x${LoremPicsumRetreiver.baseHeight}")
				.queue {
					messageID = it.id
					it.addReaction(YES).queue()
					it.addReaction(NO).queue()
					it.delete().queueAfter(2, TimeUnit.MINUTES)
				}
	}

	private fun customDimensionFormMessageSend() {
		channel.sendMessage("Scrivi la dimensione dell'immagine in pixel separata da x (esempio : 1920x1080)").queue {
			statoMachina = 2
			it.delete().queueAfter(2, TimeUnit.MINUTES)
		}
	}

	private fun grayScaleMessageSend() {
		channel.sendMessage("Vuoi l'immagine in scala di grigi? ")
				.queue {
					messageID = it.id
					it.addReaction(YES).queue()
					it.addReaction(NO).queue()
					it.delete().queueAfter(2, TimeUnit.MINUTES)
				}
	}

	private fun blurMessageSend() {
		channel.sendMessage("Vuoi l'immagine sfocata?")
				.queue {
					messageID = it.id
					it.addReaction(YES).queue()
					it.addReaction(NO).queue()
					it.delete().queueAfter(2, TimeUnit.MINUTES)
				}
	}

	private fun blurFormMessageSend() {
		channel.sendMessage("Scrivi il grado di sfocatura da 1 a 10. ").queue {
			tentativi = 0
			it.delete().queueAfter(2, TimeUnit.MINUTES)
		}
	}

	private fun executeCommand() {
		channel.sendMessage("<:downloadIcon:587591273767108628>").queue {
			it.editMessage(loremPicsumRetreiver.getImagePicsum(width, height, grayScale, blur)).queue()
		}
	}

	private fun timeoutMessage() {
		channel.sendMessage("Troppo tempo per rispondere, procedura annullata").queue {
			it.delete().queueAfter(10, TimeUnit.SECONDS)
		}
		statoMachina = 999
	}


}