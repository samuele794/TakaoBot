package command.real.BDO.RSS;

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

public class BDORSSReader {

    /**
     * Metodo per leggere un feed RSS
     * @param URL Url da cui bisogna leggere il feed
     * @return Ultimo feed registrato
     */
    public static RSSMessage readRSS(String URL){
        SyndFeedInput input = new SyndFeedInput();

        String title = null;
        String link = null;
        Document doc = null;

        try {
            URL url = new URL(URL);
            SyndFeed feed = input.build(new XmlReader(url));
            SyndEntry entry = feed.getEntries().get(0);

            title = entry.getTitle();
            link = entry.getLink();
            doc = Jsoup.parse(entry.getContents().get(0).getValue());

        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }

        return new RSSMessage(title,link,doc);
    }

    /**
     * Metodo per preparare il messaggio per discord in modalità da embedded
     * @param message Ultimo feed prelevato dal metodo di lettura
     * @return Messaggio pronto per l'invio a discord
     *
     * @see BDORSSReader#readRSS(String)  readRSS
     * @see net.dv8tion.jda.core.entities.MessageEmbed MessageEmbedded
     */

    public static MessageEmbed prepareRSStoEmbeddedMessage(RSSMessage message){
        EmbedBuilder builder = new EmbedBuilder();

        String body = Jsoup.parse(message.getDoc().toString().replaceAll("(?i)<br[^>]*>", "br2n")).text().replaceAll("br2n", "\n");
        if (!message.getDoc().select("img").isEmpty()) {
            //esite 1 immagine
            String imageUrl = message.getDoc().select("img").get(0).attr("src");
            return  builder.setTitle(message.getTitle(), message.getLink())
                    .setDescription(body).setColor(new Color(131, 196, 250)).setImage(imageUrl).build();
        } else {
            return  builder.setTitle(message.getTitle(), message.getLink()).setDescription(body).setColor(new Color(131, 196, 250)).build();
        }

    }

}
