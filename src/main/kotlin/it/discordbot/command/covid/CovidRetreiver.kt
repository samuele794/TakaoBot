package it.discordbot.command.covid

import it.discordbot.beans.covid.CovidData
import it.discordbot.command.covid.services.CovidService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.ContentsService
import org.eclipse.egit.github.core.service.RepositoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.annotation.PostConstruct

@Scope("singleton")
@Component
class CovidRetreiver {

	private val logger = LoggerFactory.getLogger(CovidRetreiver::class.java)

	@Value("\${git.hub.key}")
	private lateinit var token: String

	private lateinit var gitHubClient: GitHubClient
	private val service = RepositoryService()
	private val contentsService = ContentsService()
	private var covidItaProvince: List<String>? = null

	@PostConstruct
	private fun initCovidRetreiver() {
		val client = OkHttpClient.Builder()

		val logInterceptor = HttpLoggingInterceptor().apply {
			setLevel(HttpLoggingInterceptor.Level.BASIC)
		}
		client.addInterceptor(logInterceptor)

		retrofitClient = Retrofit.Builder()
				.baseUrl(URL_BASE)
//				.client(client.build())
				.addConverterFactory(JacksonConverterFactory.create())
				.build()
				.create(CovidService::class.java)

		gitHubClient = GitHubClient().apply {
			setOAuth2Token(token)
		}
		getProvinceListAndCache()
	}

	@Scheduled(cron = "0 0 0/23 * * *")
	private fun refreshProvinceList() {
		getProvinceListAndCache()
	}


	private final val URL_BASE = "https://enrichman.github.io/covid19/local/"

	private lateinit var retrofitClient: CovidService

	fun getCovidItalyData(region: String = "lombardia", province: String? = "Milano"): CovidData {
		return retrofitClient.getCovidItalyDataRegion(region).execute().body()!!
	}

	fun getProvinceList(): List<String>? {
		return covidItaProvince
	}

	private fun getProvinceListAndCache() {
		logger.info("Lancio ottenimento lista province COVID")
		covidItaProvince = ArrayList<String>().apply {
			contentsService
					.getContents(service.getRepository("enrichman", "covid19"),
							"local/italy")
					.map {
						if (it.name.endsWith(".json").not())
							add(it.name)
					}
		}
	}
}