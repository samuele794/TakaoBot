package it.discordbot.beans.image

import it.discordbot.command.base.StateMachine
import it.discordbot.command.image.lorempicsum.LoremPicsumRetreiver
import it.discordbot.core.EmojiContainer.Companion.NO
import it.discordbot.core.EmojiContainer.Companion.YES
import it.discordbot.core.JDAController
import it.discordbot.core.JDAController.Companion.jda
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.awt.Color
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Macchina a stati per gestire la customizzzazione della chiamata a LoremPicsum
 * @property loremPicsumRetreiver LoremPicsumRetreiver classe con i metodi per LoremPicsum
 * @property channel TextChannel canale in cui è stato invocato il comando
 * @property authorID String id Discord dell'autore del lancio del comando
 * @property width Int base dell'immagine. Default: 1920
 * @property height Int altezza dell'immagine. Default: 1080
 * @property grayScale Boolean immagine in scala di grigi
 * @property blur Int grado di sfocatura dell'immagine, valore che va da 1 a 10. Default: 0 (no blur)
 * @property messageID String id messaggi del bot, servono per i confronti per gli EventWaiter
 * @property statoMachina Int fase attuale della procedura
 * @property color Color colore per MessageEmbed
 * @property tentativi Int tentativi per risposte non conformi
 * @property fase4 Boolean controllo se la fase4 è stata completata.
 * (attualmente questa variabile è per un debito tecnico, a causa che l'EventWaiter della fase4 non viene chiuso correttamente)
 */
@Scope("prototype")
@Component
class LoremPicsumStateMachine : StateMachine {

	@Autowired
	lateinit var loremPicsumRetreiver: LoremPicsumRetreiver

    private val logger = LoggerFactory.getLogger(LoremPicsumStateMachine::class.java)

	lateinit var channel: TextChannel
	lateinit var authorID: String

	//parametri per immagine
	private var width: Int = 1920
	private var height: Int = 1080
	private var grayScale = false
	private var blur: Int = 0

	private var messageID: String = ""
	private var statoMachina = 0
	private var color = {
		val random = Random()
		Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))
	}.invoke()

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
		customDimensionMessageSend()
		JDAController.eventWaiter.waitForEvent(MessageReactionAddEvent::class.java,
				{ e -> e.member!!.user.id == authorID && e.textChannel == channel && e.messageId == messageID },
				{ e ->
					when {
						e.reactionEmote.name == YES -> {
							statoMachina = 1
							customDimensionFormMessageSend()
							updateState()
						}
						e.reactionEmote.name == NO -> {
							statoMachina = 2
							sendMessageConfiguration()
							grayScaleMessageSend()
							updateState()
						}
					}
				},
				1, TimeUnit.MINUTES, {
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
									width = if (params[0].toInt() <= 5000) params[0].toInt() else throw Exception()
									height = params[1].toInt()
									statoMachina = 2
									sendMessageConfiguration()
									grayScaleMessageSend()
								} catch (e: Exception) {
									channel.sendMessage("Parametri non conformi").queue {
										it.delete().queueAfter(5, TimeUnit.SECONDS)
									}
									tentativi++
								}

							},
							1, TimeUnit.MINUTES, {
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
						{ e -> e.member!!.user == eventGuiltReaction?.user && e.textChannel == eventGuiltReaction.channel && e.messageId == messageID },

						{ e ->
							when {
								e.reactionEmote.name == YES -> {
									statoMachina = 3
									grayScale = true
									sendMessageConfiguration()
									blurMessageSend()
									fase4 = true
								}
								e.reactionEmote.name == NO -> {
									statoMachina = 3
									grayScale = false
									sendMessageConfiguration()
									blurMessageSend()
									fase4 = true
								}
							}
						},
						1, TimeUnit.MINUTES, {
					if (!fase4) {
						timeoutMessage()
					}

				})
			}

			3 -> {
				//attesa risposta sfocatura
				JDAController.eventWaiter.waitForEvent(MessageReactionAddEvent::class.java,
						{ e -> e.member!!.user == eventGuiltReaction?.user && e.textChannel == eventGuiltReaction.channel && e.messageId == messageID },
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
						1, TimeUnit.MINUTES, {
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
									sendMessageConfiguration()
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
						1, TimeUnit.MINUTES, {
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

	private fun sendMessageConfiguration() {
		try {
			val message = EmbedBuilder().apply {
				setTitle("Parametri LoremPicsum")
				setColor(color)
				setAuthor(jda.getUserById(authorID)!!.name, null, jda.getUserById(authorID)!!.avatarUrl)
				addField("Dimensioni", width.toString() + "x" + height.toString(), true)
				addField("Scala di grigi", grayScale.toString(), true)
				addField("Grado di sfocatura", blur.toString(), true)
			}.build()
			channel.sendMessage(message).queue {
				it.delete().queueAfter(10, TimeUnit.SECONDS)
			}
		}catch (ex:Exception){
            logger.error("Errore ottenimento User $ex")
		}
	}
}