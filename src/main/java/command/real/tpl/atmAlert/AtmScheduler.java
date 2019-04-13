package command.real.tpl.atmAlert;

import beans.RSSMessage;
import beans.ServerToChannel;
import interfaces.PostgreSQLInterface;
import interfaces.TakaoLog;
import interfaces.TwitterManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;
import twitter4j.*;

import java.util.ArrayList;
import java.util.TimerTask;

import static starter.Start.jda;

public class AtmScheduler {

	private static TwitterManager manager = new TwitterManager().getTwitterManagerWithStream();
	private static String twitterUrl = "https://twitter.com/atm_informa/status/";
//	https://twitter.com/atm_informa/status/[id of status]

	public static void startAtmTweetScheduler() {
		String QUERY_PARAM = "exclude:retweets exclude:replies";
		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				if (!status.isRetweet()) {
					((Runnable) () -> {
						Status statusCopy = status;
						long id = statusCopy.getId();
						String text = statusCopy.getText();
						String mediaUrl = null;
						if (statusCopy.getMediaEntities().length != 0) {
							mediaUrl = statusCopy.getMediaEntities()[0].getMediaURL();
						}
						String profileImageUrl = statusCopy.getUser().getProfileImageURL();
						TakaoLog.logInfo("ATM Message" + id + " " + text);
						System.out.println("ATM Message" + id + " " + text);
						getMessage(id, text, mediaUrl, profileImageUrl);
					}).run();
				}

			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {

			}

			@Override
			public void onStallWarning(StallWarning warning) {

			}

			@Override
			public void onException(Exception ex) {

			}
		};
		FilterQuery query = new FilterQuery().track(QUERY_PARAM);

		query.follow(988355810);
		manager.setTwitterStreamListener(listener);
		manager.setTwitterStreamFilter(query);

	}

	public static TimerTask taskFeedRSSATM(JDA jda) {
		return new TimerTask() {
			@Override
			public void run() {
				RSSMessage message = ATMRSSReader.readRSS("https://www.atm.it/_layouts/atm/apps/PublishingRSS.aspx?web=388a6572-890f-4e0f-a3c7-a3dd463f7252&c=News%20Infomobilita");

				String lastATMAlert = PostgreSQLInterface.getLastATMAlert();

				if (lastATMAlert != null) {
					if (!lastATMAlert.equals(message.getLink())) {
						publish(message);
						PostgreSQLInterface.setLastATMAlert(message.getLink());
					}
				} else {
					publish(message);
					PostgreSQLInterface.setLastATMAlert(message.getLink());
				}


			}
		};

	}

	private static void publish(RSSMessage message) {
		ArrayList<ServerToChannel> serverList = PostgreSQLInterface.getATMAlertChannel();
		MessageEmbed messageEmbed = ATMRSSReader.prepareRSStoEmbeddedMessage(message);
		for (ServerToChannel item : serverList) {
			jda.getGuildById(item.getServerID()).
					getTextChannelById(item.getChannelID()).sendMessage(messageEmbed).queue();

		}
	}

	private static void getMessage(long tweetId, String twitterMessage, String mediaUrl, String profileImageUrl) {
		TakaoLog.logInfo("PROCESSAZIONE MESSAGE");
		String tweetUrl = twitterUrl + tweetId;

		//cambiare in contains
		if (twitterMessage.contains("#tram") ||
				twitterMessage.contains("#bus") ||
				twitterMessage.contains("#M1") ||
				twitterMessage.contains("#M2") ||
				twitterMessage.contains("#M3") ||
				twitterMessage.contains("#M5") ||
				twitterMessage.contains("\ud83d\udea6")) {
			EmbedBuilder messageEmbed = new EmbedBuilder();

			messageEmbed.setAuthor("ATM (@atm_informa)", tweetUrl, profileImageUrl)
					.setDescription(twitterMessage)
					.setFooter("Ricevuto da Twitter", "https://i.imgur.com/vkm6lHX.png");

			if (mediaUrl != null) {
				messageEmbed.setImage(mediaUrl);
			}

			ArrayList<ServerToChannel> channelList = PostgreSQLInterface.getATMAlertChannel();
			for (ServerToChannel item : channelList) {
				jda.getGuildById(item.getServerID()).getTextChannelById(item.getChannelID()).sendMessage(messageEmbed.build()).queue();
			}
		}
	}
}
