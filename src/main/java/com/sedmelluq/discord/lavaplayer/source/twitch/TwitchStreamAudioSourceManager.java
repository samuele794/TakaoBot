package com.sedmelluq.discord.lavaplayer.source.twitch;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.ExceptionTools;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.JsonBrowser;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpConfigurable;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterfaceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;

/**
 * Audio source manager which detects Twitch tracks by URL.
 */
public class TwitchStreamAudioSourceManager implements AudioSourceManager, HttpConfigurable {
	private static final String STREAM_NAME_REGEX = "^https://(?:www\\.|go\\.)?twitch.tv/([^/]+)$";
	private static final Pattern streamNameRegex = Pattern.compile(STREAM_NAME_REGEX);

	public static final String DEFAULT_CLIENT_ID = "jzkbprff40iqj646a697cyrvl0zt2m6";

	private final HttpInterfaceManager httpInterfaceManager;
	private final String twitchClientId;

	/**
	 * Create an instance.
	 */
	public TwitchStreamAudioSourceManager() {
		this(DEFAULT_CLIENT_ID);
	}

	/**
	 * Create an instance.
	 *
	 * @param clientId The Twitch client id for your application.
	 */
	public TwitchStreamAudioSourceManager(String clientId) {
		httpInterfaceManager = HttpClientTools.createDefaultThreadLocalManager();
		twitchClientId = clientId;
	}

	public String getClientId() {
		return twitchClientId;
	}

	@Override
	public String getSourceName() {
		return "twitch";
	}

	@Override
	public AudioItem loadItem(DefaultAudioPlayerManager manager, AudioReference reference) {
		String streamName = getChannelIdentifierFromUrl(reference.identifier);
		if (streamName == null) {
			return null;
		}

		JsonBrowser channelInfo = fetchStreamChannelInfo(streamName);

		if (channelInfo == null) {
			return AudioReference.NO_TRACK;
		} else {
			//Use the stream name as the display name (we would require an additional call to the user to get the true display name)
			String displayName = streamName;

			//Retrieve the data value list; this will have only one element since we're getting only one stream's information
			List<JsonBrowser> dataList = channelInfo.get("data").values();

			//The value list is empty if the stream is offline, even when hosting another channel
			if (dataList.size() == 0) {
				return null;
			}

			//The first one has the title of the broadcast
			JsonBrowser channelData = dataList.get(0);
			String status = channelData.get("title").text();

			return new TwitchStreamAudioTrack(new AudioTrackInfo(
					status,
					displayName,
					Long.MAX_VALUE,
					reference.identifier,
					true,
					reference.identifier
			), this);
		}
	}

	@Override
	public boolean isTrackEncodable(AudioTrack track) {
		return true;
	}

	@Override
	public void encodeTrack(AudioTrack track, DataOutput output) throws IOException {
		// Nothing special to do, URL (identifier) is enough
	}

	@Override
	public AudioTrack decodeTrack(AudioTrackInfo trackInfo, DataInput input) throws IOException {
		return new TwitchStreamAudioTrack(trackInfo, this);
	}

	/**
	 * Extract channel identifier from a channel URL.
	 *
	 * @param url Channel URL
	 * @return Channel identifier (for API requests)
	 */
	public static String getChannelIdentifierFromUrl(String url) {
		Matcher matcher = streamNameRegex.matcher(url);
		if (!matcher.matches()) {
			return null;
		}

		return matcher.group(1);
	}

	/**
	 * @param url Request URL
	 * @return Request with necessary headers attached.
	 */
	public HttpUriRequest createGetRequest(String url) {
		return addClientHeaders(new HttpGet(url), twitchClientId);
	}

	/**
	 * @param url Request URL
	 * @return Request with necessary headers attached.
	 */
	public HttpUriRequest createGetRequest(URI url) {
		return addClientHeaders(new HttpGet(url), twitchClientId);
	}

	/**
	 * @return Get an HTTP interface for a playing track.
	 */
	public HttpInterface getHttpInterface() {
		return httpInterfaceManager.getInterface();
	}

	@Override
	public void configureRequests(Function<RequestConfig, RequestConfig> configurator) {
		httpInterfaceManager.configureRequests(configurator);
	}

	@Override
	public void configureBuilder(Consumer<HttpClientBuilder> configurator) {
		httpInterfaceManager.configureBuilder(configurator);
	}

	private static HttpUriRequest addClientHeaders(HttpUriRequest request, String clientId) {
		request.setHeader("Client-ID", clientId);
		return request;
	}

	private JsonBrowser fetchStreamChannelInfo(String name) {
		try (HttpInterface httpInterface = getHttpInterface()) {
			HttpUriRequest request = createGetRequest("https://api.twitch.tv/helix/streams?user_login=" + name);

			return HttpClientTools.fetchResponseAsJson(httpInterface, request);
		} catch (IOException e) {
			throw new FriendlyException("Loading Twitch channel information failed.", SUSPICIOUS, e);
		}
	}

	@Override
	public void shutdown() {
		ExceptionTools.closeWithWarnings(httpInterfaceManager);
	}
}
