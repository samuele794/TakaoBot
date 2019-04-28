package command.real.sound;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
	/**
	 * Audio player for the guild.
	 */
	public final AudioPlayer player;
	/**
	 * Track scheduler for the player.
	 */
	public final TrackScheduler scheduler;
	/**
	 * Wrapper around AudioPlayer to use it as an AudioSendHandler.
	 */
	public final AudioPlayerSendHandler sendHandler;

	/**
	 * Creates a player and a track scheduler.
	 *
	 * @param manager Audio player manager to use for creating the player.
	 */
	public GuildMusicManager(AudioPlayerManager manager) {
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player);
		sendHandler = new AudioPlayerSendHandler(player);
		player.addListener(scheduler);
	}
}
