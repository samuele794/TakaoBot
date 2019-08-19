package it.discordbot.command.tpl.atm.twitter

import it.discordbot.core.EmojiContainer.Companion.CERCHIO_BARRATO
import  it.discordbot.core.EmojiContainer.Companion.SEMAFORO
import it.discordbot.core.EmojiContainer.Companion.TRIANGOLO_ALERT_GIALLO
import it.discordbot.core.EmojiContainer.Companion.YES
import it.discordbot.core.JDAController
import it.discordbot.core.TwitterManager
import it.discordbot.database.filter.ATMInterface
import net.dv8tion.jda.api.EmbedBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import twitter4j.*
import java.awt.Color
import javax.annotation.PostConstruct
import kotlin.concurrent.thread

/**
 * Classe per la schedulaziooe dei messaggi Twitter di ATM
 * @property twitterManager TwitterManager
 * @property atmInterface ATMInterface
 */
@Scope("singleton")
@Service
class ATMTwitterScheduler {

	@Autowired
	lateinit var twitterManager: TwitterManager

	@Autowired
	lateinit var atmInterface: ATMInterface

	private val logger = LoggerFactory.getLogger(ATMTwitterScheduler::class.java)

	companion object {
		private const val QUERY_PARAM = "exclude:retweets exclude:replies"
		private const val TWITTER_URL = "https://twitter.com/atm_informa/status/"
		private const val ATM_TWITTER_ID = 988355810L
		private const val TWITTER_IMAGE = "https://i.imgur.com/vkm6lHX.png"
	}

	@PostConstruct
	fun initATMTwitterScheduler() {
		val listener = object : StatusListener {
			override fun onStatus(status: Status) {
				logger.info("ATMUSERID = " + status.user.screenName + " " + status.user.id + "\nMESSAGE = " + status.text)

				logger.info("ATM IN REPLY USER ID = " + status.inReplyToUserId)
				logger.info("ATM IN REPLY STATUS ID = " + status.inReplyToStatusId)
				logger.info("ATM IN REPLY SCREEN NAME ID = " + status.inReplyToScreenName)
				if (status.user.id == ATM_TWITTER_ID && !status.isRetweet
						&& (status.inReplyToUserId == -1L || status.inReplyToUserId == ATM_TWITTER_ID)) {
					logger.info("ATMUSERID = " + status.user.screenName + " " + status.user.id + "\nMESSAGE = " + status.text + "\n PASSATO PER PROCESSAZIONE")
					thread(start = true) {

						val tweetID: Long
						var tweetMessage: String
						var mediaUrl: String? = null
						val profileImageUrl: String

						status.apply {
							tweetID = id
							tweetMessage = text
							profileImageUrl = user.profileImageURL
							if (mediaEntities.isNotEmpty()) {
								mediaUrl = mediaEntities[0].mediaURL
								if (mediaEntities.size > 1) {
									tweetMessage = "$tweetMessage\n\n Altre immagini le puoi trovare sul tweet."
								}

							}
						}
						publish(tweetID, tweetMessage, mediaUrl, profileImageUrl)
					}
				}

			}

			override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {

			}

			override fun onTrackLimitationNotice(numberOfLimitedStatuses: Int) {

			}

			override fun onScrubGeo(userId: Long, upToStatusId: Long) {

			}

			override fun onStallWarning(warning: StallWarning) {

			}

			override fun onException(ex: Exception) {

			}
		}
		val query = FilterQuery().apply {
			track(QUERY_PARAM)
			follow(ATM_TWITTER_ID)
		}

		twitterManager.apply {
			setTwitterStreamListener(listener)
			setTwitterStreamFilter(query)
		}
	}


	private fun publish(tweetID: Long, twitterMessage: String, mediaUrl: String?, profileImageUrl: String) {
		val tweetUrl = TWITTER_URL + tweetID
		if (twitterMessage.contains("#tram", ignoreCase = true) ||
				twitterMessage.contains("#bus", ignoreCase = true) ||
				twitterMessage.contains("#M1", ignoreCase = true) ||
				twitterMessage.contains("#M2", ignoreCase = true) ||
				twitterMessage.contains("#M3", ignoreCase = true) ||
				twitterMessage.contains("#M5", ignoreCase = true) ||
				twitterMessage.contains(SEMAFORO) ||
				twitterMessage.contains(TRIANGOLO_ALERT_GIALLO) ||
				twitterMessage.contains(YES) ||
				twitterMessage.contains(CERCHIO_BARRATO) ||
				twitterMessage.contains("sciopero", ignoreCase = true) ||
				twitterMessage.contains("manifestazione", ignoreCase = true) ||
				twitterMessage.contains("aggiornamento", ignoreCase = true)||
				twitterMessage.contains("suicidio", ignoreCase = true)) {
			logger.info("INIZIO PUBBLICAZIONE ATM TWITTER")

			val message = EmbedBuilder().apply {
				setAuthor("ATM (@atm_informa)", tweetUrl, profileImageUrl)
				setDescription(twitterMessage)
				setColor(Color(244, 131, 37))
				setFooter("Ricevuto da Twitter", TWITTER_IMAGE)
				if (mediaUrl != null) {
					setImage(mediaUrl)
				}
			}.build()

			val serverList = atmInterface.getATMAlertChannels()
			for (server in serverList) {
				JDAController.jda.getGuildById(server.serverID)
						?.getTextChannelById(server.channelID)
						?.sendMessage(message)
						?.queue()
				//TODO GENERALIZZARE IL METODO DI PUBBLICAZIONE
			}
		}
	}
}