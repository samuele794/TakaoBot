package command.real.tpl.atmAlert;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class TwitterManager {

	private TwitterStreamFactory twitterStreamFactory = null;
	private TwitterStream twitterStream = null;

	public TwitterManager getTwitterManagerWithStream() {
		if (twitterStreamFactory == null) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(new File(AtmScheduler.class.getClassLoader().getResource("twitter4j.properties").toURI()));
			} catch (FileNotFoundException | URISyntaxException e) {
				e.printStackTrace();
			}

			Properties prop = new Properties();
			try {
				prop.load(input);
			} catch (IOException e) {
				e.printStackTrace();
			}

			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
					.setOAuthConsumerKey(prop.getProperty("oauth.consumerKey"))
					.setOAuthConsumerSecret(prop.getProperty("oauth.consumerSecret"))
					.setOAuthAccessToken(prop.getProperty("oauth.accessToken"))
					.setOAuthAccessTokenSecret(prop.getProperty("oauth.accessTokenSecret"));
			this.twitterStreamFactory = new TwitterStreamFactory(cb.build());
			this.twitterStream = twitterStreamFactory.getInstance();
		}
		return this;
	}

	public TwitterManager setTwitterStreamListener(StatusListener listener) {
		twitterStream.addListener(listener);
		return this;
	}

	public TwitterManager setTwitterStreamFilter(FilterQuery filterQuery) {
		twitterStream.filter(filterQuery);
		return this;
	}

	public TwitterStream getTwitterStream() {
		return twitterStream;
	}
}
