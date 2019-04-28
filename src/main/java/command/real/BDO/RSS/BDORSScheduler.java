package command.real.BDO.RSS;

import beans.RSSMessage;
import beans.ServerToChannel;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.TimerTask;

public class BDORSScheduler {


	public static TimerTask taskFeedRSSBDO(JDA jda) {
		return new TimerTask() {
			@Override
			public void run() {


				RSSMessage rssNewsMessage = BDORSSReader.readRSS("https://community.blackdesertonline.com/index.php?forums/news-announcements.181/index.rss");
				RSSMessage rssPatchMessage = BDORSSReader.readRSS("https://community.blackdesertonline.com/index.php?forums/patch-notes.5/index.rss");

				ArrayList<String> newsBDOList = PostgreSQLInterface.getListNewsBDO();
				//publishRSSNews
				if (newsBDOList != null) {
					if (newsBDOList.indexOf(rssNewsMessage.getLink()) == -1) {
						procedurePublish(rssNewsMessage, newsBDOList, jda);
					}
				} else {
					newsBDOList = new ArrayList<>();
					procedurePublish(rssNewsMessage, newsBDOList, jda);
				}

				//publishRSSPatch
				if (PostgreSQLInterface.getLastPatchBDO() != null) {
					if (!PostgreSQLInterface.getLastPatchBDO().equals(rssPatchMessage.getLink())) {
						MessageEmbed patchMessage = BDORSSReader.prepareRSStoEmbeddedMessage(rssPatchMessage);
						ArrayList<ServerToChannel> patchhNews = PostgreSQLInterface.getBDOPatchChannel();
						publishMessage(patchMessage, patchhNews, jda);
						PostgreSQLInterface.setLastPatchBDO(rssPatchMessage.getLink());
					}
				} else {
					MessageEmbed patchMessage = BDORSSReader.prepareRSStoEmbeddedMessage(rssPatchMessage);
					ArrayList<ServerToChannel> patchhNews = PostgreSQLInterface.getBDOPatchChannel();
					publishMessage(patchMessage, patchhNews, jda);
					PostgreSQLInterface.setLastPatchBDO(rssPatchMessage.getLink());
				}
			}
		};
	}

	private static void procedurePublish(RSSMessage rssNewsMessage, ArrayList<String> newsBDOList, JDA jda) {
		MessageEmbed newsMessage = BDORSSReader.prepareRSStoEmbeddedMessage(rssNewsMessage);   //ottieni il messaggio embedded
		ArrayList<ServerToChannel> listNews = PostgreSQLInterface.getBDONewsChannel();         //ottieni la delle ultime news già pubblicate
		publishMessage(newsMessage, listNews, jda);                                         //publish del messaggio
		newsBDOList.add(rssNewsMessage.getLink());                                          //aggiunta dell'ultima news alla lista
		PostgreSQLInterface.setNewsBDO(newsBDOList);                                           //salvataggio su db
	}

	private static void publishMessage(MessageEmbed newsMessage, ArrayList<ServerToChannel> servers, JDA jda) {
		for (ServerToChannel obj : servers) {
			String channelID = obj.getChannelID();
			String serverID = obj.getServerID();
			jda.getGuildById(serverID).getTextChannelById(channelID).sendMessage(newsMessage).queue();
		}
	}
}
