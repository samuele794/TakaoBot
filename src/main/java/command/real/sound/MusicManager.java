package command.real.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import interfaces.TakaoLog;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.*;

public class MusicManager {

	private static final int DEFAULT_VOLUME = 100; //(0 - 150, where 100 is default max volume)
	private final Map<String, GuildMusicManager> musicManagers;
	private AudioPlayerManager playerManager;

	public MusicManager() {

		this.playerManager = new DefaultAudioPlayerManager();
		playerManager.registerSourceManager(new YoutubeAudioSourceManager());
		playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
//		playerManager.registerSourceManager(new BandcampAudioSourceManager());
//		playerManager.registerSourceManager(new VimeoAudioSourceManager());
		playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
		playerManager.registerSourceManager(new HttpAudioSourceManager());
		playerManager.registerSourceManager(new LocalAudioSourceManager());

		musicManagers = new HashMap<>();
	}

	private static String getUrl(MessageReceivedEvent event) {
		List<String> listMessage = Arrays.asList(event.getMessage().getContentRaw().split(" "));
		return listMessage.get(listMessage.size() - 1);
	}

	private static String getTimestamp(long milliseconds) {
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

		if (hours > 0)
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		else
			return String.format("%02d:%02d", minutes, seconds);
	}


	public void join(MessageReceivedEvent event) {

		Guild guild = event.getGuild();
		GuildMusicManager guildMusicManager = getMusicManager(guild.getId());

		VoiceChannel voiceChannel;
		if (event.getMember().getVoiceState().inVoiceChannel()) {
			voiceChannel = event.getMember().getVoiceState().getChannel();
			guild.getAudioManager().setSendingHandler(guildMusicManager.sendHandler);

			try {
				guild.getAudioManager().openAudioConnection(voiceChannel);
			} catch (PermissionException e) {
				if (e.getPermission() == Permission.VOICE_CONNECT) {
					event.getChannel().sendMessage("Accesso al canale negato").queue();
				}

				TakaoLog.logError("Problemi accesso al canale " + event.getChannel().getName() + "del server" +
						event.getGuild().getName());
			}
		} else {
			event.getChannel().sendMessage("Entra in un canale vocale, per favore").queue();
		}

	}

	//controllato
	public void leave(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());

		guildMusicManager.scheduler.getQueue().clear();
		guildMusicManager.player.stopTrack();
		guildMusicManager.player.setPaused(false);
		event.getGuild().getAudioManager().setSendingHandler(null);
		event.getGuild().getAudioManager().closeAudioConnection();
	}

	public void play(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());
		Guild guild = event.getGuild();
		if (guild.getAudioManager().getSendingHandler() == null) {
			this.join(event);
		}

		if (event.getMessage().isMentioned(event.getJDA().getSelfUser())) {

			if ((new StringTokenizer(event.getMessage().getContentRaw())).countTokens() == 3) {
				//menzionato e parametri a 3

				loadAndPlay(guildMusicManager, event.getChannel(), getUrl(event), false);
			} else {
				event.getChannel().sendMessage("Quantità di parametri non conformi").queue();
			}
		} else {
			if ((new StringTokenizer(event.getMessage().getContentRaw())).countTokens() == 2) {

				loadAndPlay(guildMusicManager, event.getChannel(), getUrl(event), false);
			} else {
				event.getChannel().sendMessage("Quantità di parametri non conformi").queue();
			}
		}
	}

	public void playPlaylist(MessageReceivedEvent event) {
		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());

		loadAndPlay(guildMusicManager, event.getChannel(), getUrl(event), true);
	}

	//controllato
	public void skip(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());
		guildMusicManager.scheduler.nextTrack();
		event.getChannel().sendMessage("Canzone saltata").queue();
	}

	// controllato
	public void pauseResume(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());
		AudioPlayer player = guildMusicManager.player;

		if (player.getPlayingTrack() == null) {
			event.getChannel().sendMessage("Non è possibile mettere in pausa o riprendere il nulla, coglione.").queue();
			return;
		}

		player.setPaused(!player.isPaused());
		if (player.isPaused())
			event.getChannel().sendMessage("Riproduzione in pausa").queue();
		else
			event.getChannel().sendMessage("Ripresa riproduzione").queue();
	}

	//controllato
	public void stop(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());

		guildMusicManager.scheduler.getQueue().clear();
		guildMusicManager.player.stopTrack();
		guildMusicManager.player.setPaused(false);
		event.getChannel().sendMessage("Riproduzione stoppata e playlist svuotata").queue();

	}

	private void changeVolume(MessageReceivedEvent event) {
//		if (command.length == 1) {
//			event.getChannel().sendMessage("Current player volume: **" + player.getVolume() + "**").queue();
//		} else {
//			try {
//				int newVolume = Math.max(10, Math.min(100, Integer.parseInt(command[1])));
//				int oldVolume = player.getVolume();
//				player.setVolume(newVolume);
//				event.getChannel().sendMessage("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`").queue();
//			} catch (NumberFormatException e) {
//				event.getChannel().sendMessage("`" + command[1] + "` is not a valid integer. (10 - 100)").queue();
//			}
//		}
	}

	public void restart(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());

		AudioTrack track = guildMusicManager.player.getPlayingTrack();
		if (track == null)
			track = guildMusicManager.scheduler.lastTrack;

		if (track != null) {
			event.getChannel().sendMessage("Riavvio traccia: " + track.getInfo().title).queue();
			guildMusicManager.player.playTrack(track.makeClone());
		} else {
			event.getChannel().sendMessage("No track has been previously started, so the player cannot replay a track!").queue();
		}
	}

	public void repeat(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());
		guildMusicManager.scheduler.setRepeating(!guildMusicManager.scheduler.isRepeating());
		event.getChannel().sendMessage("Riproduzione impostata su: **" + (guildMusicManager.scheduler.isRepeating() ? "ripeti" : "non ripetere") + "**").queue();

	}

	public void reset(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());

		synchronized (musicManagers) {
			guildMusicManager.scheduler.getQueue().clear();
			guildMusicManager.player.destroy();
			event.getGuild().getAudioManager().setSendingHandler(null);
			musicManagers.remove(event.getGuild().getId());
		}

		event.getGuild().getAudioManager().setSendingHandler(guildMusicManager.sendHandler);
		event.getChannel().sendMessage("Riproduttore resettato").queue();
	}

	public void nowPlay(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());

		AudioTrack currentTrack = guildMusicManager.player.getPlayingTrack();
		if (currentTrack != null) {
			String title = currentTrack.getInfo().title;
			String position = getTimestamp(currentTrack.getPosition());
			String duration = getTimestamp(currentTrack.getDuration());

			String nowplaying = String.format("**In Riprodzione:** %s\n**Time:** [%s / %s]",
					title, position, duration);

			event.getChannel().sendMessage(nowplaying).queue();
		} else
			event.getChannel().sendMessage("Non sto riproducendo nulla!").queue();
	}

	public void getListQueue(MessageReceivedEvent event) {

		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());
		Queue<AudioTrack> queue = guildMusicManager.scheduler.getQueue();

		synchronized (queue) {
			if (queue.isEmpty()) {
				event.getChannel().sendMessage("La coda è vuota").queue();
			} else {
				int trackCount = 0;
				long queueLength = 0;
				StringBuilder sb = new StringBuilder();
				sb.append("Coda attuale: ").append(queue.size()).append("\n");
				for (AudioTrack track : queue) {
					queueLength += track.getDuration();
					if (trackCount < 10) {
						sb.append("`[").append(getTimestamp(track.getDuration())).append("]` ");
						sb.append(track.getInfo().title).append("\n");
						trackCount++;
					}
				}
				sb.append("\n").append("Tempo totale di coda: ").append(getTimestamp(queueLength));

				event.getChannel().sendMessage(sb.toString()).queue();
			}
		}
	}

	public void shuffleQueue(MessageReceivedEvent event) {


		GuildMusicManager guildMusicManager = getMusicManager(event.getGuild().getId());

		if (guildMusicManager.scheduler.getQueue().isEmpty()) {
			event.getChannel().sendMessage("La coda è attualmente vuota!").queue();
			return;
		}

		guildMusicManager.scheduler.shuffle();
		event.getChannel().sendMessage("Coda mischiata").queue();
	}

	private void loadAndPlay(GuildMusicManager mng, final MessageChannel channel, String url, final boolean addPlaylist) {
		final String trackUrl;

		//Strip <>'s that prevent discord from embedding link resources
		if (url.startsWith("<") && url.endsWith(">"))
			trackUrl = url.substring(1, url.length() - 1);
		else
			trackUrl = url;

		playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				String msg = "Aggiunta alla coda: " + track.getInfo().title;
				if (mng.player.getPlayingTrack() == null)
					msg += "\ne inizio riproduzione";

				mng.scheduler.queue(track);
				channel.sendMessage(msg).queue();
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				List<AudioTrack> tracks = playlist.getTracks();


				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}

				if (addPlaylist) {
					channel.sendMessage("Aggiunte **" + playlist.getTracks().size() + "** tracce alla coda").queue();
					tracks.forEach(mng.scheduler::queue);
				} else {
					channel.sendMessage("Aggiunto alla coda " + firstTrack.getInfo().title).queue();
					mng.scheduler.queue(firstTrack);
				}
			}

			@Override
			public void noMatches() {
				channel.sendMessage("Non ho abbastanza forza di vedere dentro a " + trackUrl).queue();
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Non è nel mio potere: " + exception.getMessage()).queue();
			}
		});
	}

	private GuildMusicManager getMusicManager(String guildId) {
		GuildMusicManager mng = musicManagers.get(guildId);
		if (mng == null) {
			synchronized (musicManagers) {
				mng = musicManagers.get(guildId);
				if (mng == null) {
					mng = new GuildMusicManager(playerManager);
					mng.player.setVolume(DEFAULT_VOLUME);
					musicManagers.put(guildId, mng);
				}
			}
		}
		return mng;
	}
}
