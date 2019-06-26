package it.discordbot.beans.riot

/*@Scope("singleton")
@Component*/
class RiotManager {
	//	@Value("\${riot.api.key}")
	lateinit var riotApiKey: String

	enum class Region(url: String) {
		EUW("http://euw1.api.riotgames.com"),
		EUNE("http://eun1.api.riotgames.com"),
		NA("http://na1.api.riotgames.com")
	}
}