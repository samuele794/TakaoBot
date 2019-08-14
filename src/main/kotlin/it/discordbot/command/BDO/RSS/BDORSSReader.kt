package it.discordbot.command.BDO.RSS

import com.rometools.rome.io.FeedException
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import it.discordbot.beans.RSSMessage
import it.discordbot.command.base.RSSReader
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.awt.Color
import java.net.URL
import java.util.regex.Pattern

/**
 * Classe per leggere gli RSS di BDO
 */
@Scope("singleton")
@Component
class BDORSSReader : RSSReader {

	/**
	 * Metodo per leggere un feed RSS
	 * @param url Url da cui bisogna leggere il feed
	 * @return Ultimo feed registrato
	 */
	@Throws(FeedException::class)
	override fun readRSS(url: String): RSSMessage {

		var title: String
		var link: String
		var doc: Document

		SyndFeedInput().apply {
			build(XmlReader(URL(url))).apply {
				entries.first().apply {
					title = getTitle()
					link = getLink()
					doc = Jsoup.parse(contents.first().value)
				}
			}
		}
		return RSSMessage(title, link, doc)
	}

	private fun linkNumberParser(link: String): Int {
		return link.substring(1, link.length - 1).toInt()
	}

	/**
	 * Metodo per confrontare se una news o una patch di BDO è nuova
	 * rispetto all'ultima pubblicata dal bot
	 *
	 * @param linkRSS String
	 * @param linkDB String
	 * @return Boolean
	 */
	fun isNew(linkRSS: String, linkDB: String): Boolean {
		val regex1 = Pattern.compile(
				"\\.(\\d+)/?\$",
				Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.COMMENTS)

		val regex2 = Pattern.compile(
				"\\.(\\d+)/?\$",
				Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.COMMENTS)

		val regexMatcher1 = regex1.matcher(linkRSS)
		regexMatcher1.find()

		val numberRSS = linkNumberParser(regexMatcher1.group())

		val regexMatcher2 = regex2.matcher(linkDB)
		regexMatcher2.find()

		val numberDB = linkNumberParser(regexMatcher2.group())
		return if (numberRSS == numberDB) {
			false
		} else {
			numberDB < numberRSS
		}
	}

	override fun prepareRSStoMessageEmbed(message: RSSMessage): MessageEmbed {
		val body = Jsoup.parse(message.doc.toString().replace("<br>", "br2n")).text().replace("br2n", "\n")
		if (!message.doc.select("img").isEmpty()) {
			//esite 1 immagine
			val imageUrl = message.doc.select("img")[0].attr("src")
			return EmbedBuilder().apply {
				setTitle(message.title, message.link)
				setDescription(body)
				setColor(Color(131, 196, 250))
				setImage(imageUrl)
			}.build()
		} else {
			return EmbedBuilder().apply {
				setTitle(message.title, message.link)
				setDescription(body)
				setColor(Color(131, 196, 250))
			}.build()
		}
	}

}