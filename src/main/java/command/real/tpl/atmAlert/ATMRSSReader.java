package command.real.tpl.atmAlert;

import beans.RSSMessage;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATMRSSReader {

	public static RSSMessage readRSS(String URL) {
		SyndFeedInput input = new SyndFeedInput();

		String title = null;
		String link = null;
		Document doc = null;

		try {
			URL url = new URL(URL);
			SyndFeed feed = input.build(new XmlReader(url));
			List<SyndEntry> entryList = feed.getEntries();

			SyndEntry entry = entryList.get(entryList.size() - 1);


			title = entry.getTitle();
			link = entry.getLink();
			doc = Jsoup.parse(entry.getDescription().getValue());

		} catch (FeedException | IOException e) {
			e.printStackTrace();
		}

		return new RSSMessage(title, link, doc);
	}

	public static MessageEmbed prepareRSStoEmbeddedMessage(RSSMessage message) {
		EmbedBuilder builder = new EmbedBuilder();

		Pattern regex = Pattern.compile(
				"</h3>|</p>|<br>",
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
		Matcher regexMatcher = regex.matcher(message.getDoc().toString());
		String mess = regexMatcher.replaceAll("br2n");

		regex = Pattern.compile(
				"<h3>|<p>",
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
		regexMatcher = regex.matcher(mess);
		mess = regexMatcher.replaceAll("");


		regex = Pattern.compile(
				"<strong>|</strong>",
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
		regexMatcher = regex.matcher(mess);
		mess = regexMatcher.replaceAll("**");

		mess = Jsoup.parse(mess).text().replaceAll("br2n", "\n");

		if (mess.length() >= 2048){
			mess = new StringBuilder().append( mess.subSequence(0, 2000).toString()).append("\n\n Continua nel link...").toString();
		}

		return builder.setTitle(message.getTitle(), message.getLink())
				.setThumbnail("https://i.imgur.com/2cMpNuI.png")
				.setDescription(mess).setColor(new Color(244, 131, 37)).build();


	}
}
