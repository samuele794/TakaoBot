package it.discordbot.beans.riot.dao.call

import it.discordbot.beans.riot.RiotManager
import okhttp3.OkHttpClient
import okhttp3.Request


class SummonerCall {


	lateinit var riotManager: RiotManager


	companion object {
		//  /lol/summoner/v4/summoners/by-name/{summonerName}

		/*
			Codici errori:
			429		Rate limit exceeded
			404		Data not found
		*/
		const val GET_BY_NAME_ENDPOINT = "/lol/summoner/v4/summoners/by-name/"

		// /lol/summoner/v4/summoners/{encryptedSummonerId}
		const val GET_BY_SUMMONER_ID_ENDPOINT = "/lol/summoner/v4/summoners/"
	}

	fun getSummonerByName(summonerName: String, region: RiotManager.Region) {
		val resp = OkHttpClient().newCall(
				Request.Builder().let {
					it.url(StringBuilder().apply {
						append(region)
						append(GET_BY_NAME_ENDPOINT)
						append("?")
						append(riotManager.riotApiKey)
					}.toString())
				}.build()
		).execute()

		when (resp.code()) {
			200 -> {

			}

			429 -> {

			}
			404 -> {

			}
		}
	}
}