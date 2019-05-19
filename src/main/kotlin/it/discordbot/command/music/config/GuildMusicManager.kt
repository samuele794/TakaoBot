package it.discordbot.command.music.config

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager

class GuildMusicManager(val manager: AudioPlayerManager) {

	var player: AudioPlayer

	var scheduler: TrackScheduler

	var sendHandler: AudioPlayerSendHandler

	init {
		player = manager.createPlayer()
		scheduler = TrackScheduler(player)
		sendHandler = AudioPlayerSendHandler(player)
		player.addListener(scheduler)
	}

}