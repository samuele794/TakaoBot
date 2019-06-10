package it.discordbot.command.image.lorempicsum

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component

@Component
class LoremPicsumRetreiver {

	companion object {
		const val baseWidth = 1920
		const val baseHeight = 1080
	}

	private final val urlBase = "https://picsum.photos/"


	private val client = OkHttpClient()

	fun getImagePicsum(width: Int = baseWidth, height: Int = baseHeight, grayScale: Boolean = false, blur: Int = 0): String {
		val url = buildString {
			append("$urlBase$width/$height/?")
			if (grayScale) {
				append("grayscale")
			}
			when {
				//true        true
				grayScale && blur != 0 -> {
					append("&")
					append("blur=$blur")
				}
				!grayScale && blur != 0 -> {
					append("blur=$blur")
				}
			}
		}
		val urlResponse: String
		client.newCall(Request.Builder().apply {
			url(url)
		}.build()).execute().apply {
			urlResponse = request().url().toString()
		}.close()
		return urlResponse
	}


}