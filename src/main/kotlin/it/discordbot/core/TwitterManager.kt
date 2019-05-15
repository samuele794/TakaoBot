package it.discordbot.core

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import twitter4j.FilterQuery
import twitter4j.StatusListener
import twitter4j.TwitterStream
import twitter4j.TwitterStreamFactory
import twitter4j.conf.ConfigurationBuilder
import javax.annotation.PostConstruct

@Scope("singleton")
@Component
class TwitterManager {

	@Value("\${oauth.consumerKey}")
	lateinit var twitterConsumerKey: String

	@Value("\${oauth.consumerSecret}")
	lateinit var twitterConsumerSecret: String

	@Value("\${oauth.accessToken}")
	lateinit var twitterAccessToken: String

	@Value("\${oauth.accessTokenSecret}")
	lateinit var twitterAccessTokenSecret: String

	private lateinit var twitterStreamFactory: TwitterStreamFactory
	private lateinit var twitterStream: TwitterStream

	@PostConstruct
	fun initTwitter() {
		val configuration = ConfigurationBuilder().apply {
			setOAuthConsumerKey(twitterConsumerKey)
			setOAuthConsumerSecret(twitterConsumerSecret)
			setOAuthAccessToken(twitterAccessToken)
			setOAuthAccessTokenSecret(twitterAccessTokenSecret)
		}.build()

		twitterStreamFactory = TwitterStreamFactory(configuration)
		twitterStream = twitterStreamFactory.instance
	}

	fun setTwitterStreamListener(listener: StatusListener): TwitterManager {
		twitterStream.addListener(listener)
		return this
	}

	fun setTwitterStreamFilter(filterQuery: FilterQuery): TwitterManager {
		twitterStream.filter(filterQuery)
		return this
	}

	fun getTwitterStream(): TwitterStream {
		return twitterStream
	}

}