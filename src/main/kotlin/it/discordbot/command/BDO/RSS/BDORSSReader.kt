package it.discordbot.command.BDO.RSS

import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import it.discordbot.beans.RSSMessage
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.awt.Color
import java.net.URL

@Scope("singleton")
@Component
class BDORSSReader {

	/**
	 * Metodo per leggere un feed RSS
	 * @param url Url da cui bisogna leggere il feed
	 * @return Ultimo feed registrato
	 */
	fun readRSS(url: String): RSSMessage {

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

	fun prepareRSStoEmbeddedMessage(message: RSSMessage): MessageEmbed {
		val body = Jsoup.parse(message.doc.toString().replace("(?i)<br[^>]*>", "br2n"))
				.text()
				.replace("br2n".toRegex(), "\n")
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