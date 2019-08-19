package it.discordbot.command.image.lorempicsum

import it.discordbot.command.image.lorempicsum.connection.LoremPicsumService
import org.springframework.stereotype.Component
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Classe che si occupa di ottenere il link delle immagini dal LoremPicsum
 * @property URL_BASE String
 */
@Component
class LoremPicsumRetreiver {

	companion object {
		const val baseWidth = 1920
		const val baseHeight = 1080
	}

	private final val URL_BASE = "https://picsum.photos/"

	private val retrofitClient = Retrofit.Builder()
			.baseUrl(URL_BASE)
			.addConverterFactory(ScalarsConverterFactory.create())
			.build()
			.create(LoremPicsumService::class.java)

	fun getImagePicsum(width: Int = baseWidth, height: Int = baseHeight, grayScale: Boolean = false, blur: Int = 0): String {
		var urlImage = ""
		when {
			!grayScale && blur == 0 -> {
				urlImage = retrofitClient.getImageRandom(width, height).execute().getRequestUrl()
			}

			grayScale && blur == 0 -> {
				urlImage = retrofitClient.getImageRandomGrayScale(width, height).execute().getRequestUrl()
			}

			grayScale && blur != 0 -> {
				urlImage = retrofitClient.getImageRandomGrayScaleBlur(width, height, blur).execute().getRequestUrl()

			}
		}

		return urlImage

	}

	private fun <T> Response<T>.getRequestUrl(): String {
		return this.raw().request().url().toString()
	}

}

