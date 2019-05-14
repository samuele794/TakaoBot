package it.discordbot.command.tpl.atmAlert

import it.discordbot.core.JDAController
import it.discordbot.core.TakaoLog
import it.discordbot.core.TwitterManager
import it.discordbot.database.interfaces.ATMInterface
import net.dv8tion.jda.core.EmbedBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import twitter4j.*
import java.awt.Color
import javax.annotation.PostConstruct
import kotlin.concurrent.thread

@Scope("singleton")
@Service
class ATMTwitterScheduler {

	@Autowired
	lateinit var twitterManager: TwitterManager

	@Autowired
	lateinit var atmInterface: ATMInterface

	companion object {
		private const val QUERY_PARAM = "exclude:retweets exclude:replies"
		private const val TWITTER_URL = "https://twitter.com/atm_informa/status/"
		private const val ATM_TWITTER_ID = 988355810L
		private const val TWITTER_IMAGE = "https://i.imgur.com/vkm6lHX.png"
	}

	@PostConstruct
	fun initATMTwitterScheduler(){
		val listener = object : StatusListener {
			override fun onStatus(status: Status) {
				if (status.user.id == ATM_TWITTER_ID) {

					if (!status.isRetweet) {
						TakaoLog.logInfo("ATMUSERID = " + status.user.screenName + " " + status.user.id + "\nMESSAGE = " + status.text)
						thread(start = true) {
							val statusCopy = status

							val tweetID: Long
							val tweetMessage: String
							var mediaUrl: String? = null
							val profileImageUrl: String

							statusCopy.apply {
								tweetID = id
								tweetMessage = text
								profileImageUrl = user.profileImageURL
								if (mediaEntities.isNotEmpty()) {
									mediaUrl = mediaEntities[0].mediaURL
								}
							}

							publish(tweetID, tweetMessage, mediaUrl, profileImageUrl)

						}
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
		var query = FilterQuery().track(QUERY_PARAM)

		query.follow(ATM_TWITTER_ID)

		twitterManager.apply {
			setTwitterStreamListener(listener)
			setTwitterStreamFilter(query)
		}
	}


	private fun publish(tweetID: Long, twitterMessage: String, mediaUrl: String?, profileImageUrl: String) {
		val tweetUrl = TWITTER_URL + tweetID
		if (twitterMessage.contains("#tram") ||
				twitterMessage.contains("#bus") ||
				twitterMessage.contains("#M1") ||
				twitterMessage.contains("#M2") ||
				twitterMessage.contains("#M3") ||
				twitterMessage.contains("#M5") ||
				twitterMessage.contains("\ud83d\udea6")) {
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
						.getTextChannelById(server.channelID)
						.sendMessage(message)
						.queue()
			}
		}
	}
}