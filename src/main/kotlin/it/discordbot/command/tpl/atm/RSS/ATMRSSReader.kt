package it.discordbot.command.tpl.atm.RSS

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
 * Classe per leggere il feed RSS di atm
 */
@Scope("singleton")
@Component
class ATMRSSReader : RSSReader {

	override fun readRSS(url: String): RSSMessage {

		var title: String
		var link: String
		var doc: Document

		SyndFeedInput().apply {
			build(XmlReader(URL(url))).let {
				it.entries.last().apply {
					title = getTitle()
					link = getLink()
					doc = Jsoup.parse(description.value)
				}
			}
		}
		return RSSMessage(title, link, doc)
	}

	override fun prepareRSStoMessageEmbed(message: RSSMessage): MessageEmbed {
		var regex = Pattern.compile(
				"</h3>|</p>|<br>",
				Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.COMMENTS)
		var regexMatcher = regex.matcher(message.doc.toString())
		var mess = regexMatcher.replaceAll("br2n")

		regex = Pattern.compile(
				"<h3>|<p>",
				Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.COMMENTS)
		regexMatcher = regex.matcher(mess)
		mess = regexMatcher.replaceAll("")

		regex = Pattern.compile(
				"<strong>|</strong>",
				Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.COMMENTS)
		regexMatcher = regex.matcher(mess)
		mess = regexMatcher.replaceAll("**")

		mess = Jsoup.parse(mess).text().replace("br2n".toRegex(), "\n")

		if (mess.length >= 2048) {
			mess = StringBuilder().append(mess.subSequence(0, 2000).toString()).append("\n\n Continua nel link...").toString()
		}

		return EmbedBuilder().apply {
			setTitle(message.title, message.link)
			setThumbnail(ATM_LOGO_RSS)
			setDescription(mess)
			setColor(Color(244, 131, 37))
		}.build()

	}

	companion object {
		private const val ATM_LOGO_RSS = "https://i.imgur.com/2cMpNuI.png"
	}
}