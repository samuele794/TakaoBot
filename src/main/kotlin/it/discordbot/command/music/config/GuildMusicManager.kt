package it.discordbot.command.music.config

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager

class GuildMusicManager(manager: AudioPlayerManager) {

	var player: AudioPlayer = manager.createPlayer()

	var scheduler: TrackScheduler = TrackScheduler(player)

	var sendHandler: AudioPlayerSendHandler = AudioPlayerSendHandler(player)

	var commandChannelID: String? = null

	init {
		player.addListener(scheduler)
	}

}