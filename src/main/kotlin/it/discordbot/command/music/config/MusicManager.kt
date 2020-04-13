package it.discordbot.command.music.config

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.PermissionException
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Consumer

@Scope("singleton")
@Component
class MusicManager {

	private val playerManager: AudioPlayerManager
	private val DEFAULT_VOLUME = 25 //(0 - 150, where 100 is default max volume)
	private val musicManagers: MutableMap<String, GuildMusicManager>

	init {
		playerManager = DefaultAudioPlayerManager()
		playerManager.registerSourceManager(YoutubeAudioSourceManager())
		playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault())
//		playerManager.registerSourceManager(new BandcampAudioSourceManager());
//		playerManager.registerSourceManager(new VimeoAudioSourceManager());
		playerManager.registerSourceManager(TwitchStreamAudioSourceManager())
		playerManager.registerSourceManager(HttpAudioSourceManager())
		playerManager.registerSourceManager(LocalAudioSourceManager())

		musicManagers = HashMap()
	}

	/**
	 * Parsing del messaggio per ottenere l'url
	 *
	 * @param event
	 * @return String url della canzone
	 */
	private fun getUrl(event: MessageReceivedEvent): String {
		val listMessage = listOf(*event.message.contentRaw.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
		return listMessage[listMessage.size - 1]
	}

	private fun getTimestamp(milliseconds: Long): String {
		val seconds = (milliseconds / 1000).toInt() % 60
		val minutes = (milliseconds / (1000 * 60) % 60).toInt()
		val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()

		return if (hours > 0)
			String.format("%02d:%02d:%02d", hours, minutes, seconds)
		else
			String.format("%02d:%02d", minutes, seconds)
	}

	/**
	 * Metodo per eseguire il join nel canale
	 *
	 * @param event
	 */
	private fun join(event: MessageReceivedEvent) {

		val guild = event.guild
		val guildMusicManager = getMusicManager(guild.id)

		val voiceChannel: VoiceChannel
		if (event.member!!.voiceState!!.inVoiceChannel()) {
			voiceChannel = event.member!!.voiceState!!.channel!!
			guild.audioManager.sendingHandler = guildMusicManager.sendHandler

			try {
				guild.audioManager.openAudioConnection(voiceChannel)
			} catch (e: PermissionException) {
				if (e.permission == Permission.VOICE_CONNECT) {
					event.channel.sendMessage("Accesso al canale negato").queue()
				}

//				TakaoLog.logError("Problemi accesso al canale " + event.channel.name + "del server" +
//						event.guild.name)
			}

		} else {
			event.channel.sendMessage("Entra in un canale vocale, per favore").queue()
		}

	}

	/**
	 * Metodo per far uscire il bot dalla lobby, bloccherà la queue
	 * e la svuoterà
	 *
	 * @param event
	 */
	internal fun leave(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)
		if (!guildMusicManager.scheduler.queue.isEmpty()) {
			guildMusicManager.scheduler.queue.clear()
		}

		guildMusicManager.player.stopTrack()
		guildMusicManager.player.isPaused = false
		guildMusicManager.player.destroy()
		event.guild.audioManager.sendingHandler = null
		event.guild.audioManager.closeAudioConnection()
	}

	/**
	 * Metodo per riprodurre l'url audio passato come parametro
	 * @param event
	 */
	internal fun play(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)
		val guild = event.guild
		guildMusicManager.commandChannelID = event.textChannel.id
		if (guild.audioManager.sendingHandler == null) {
			this.join(event)
		}

		if (event.message.isMentioned(event.jda.selfUser)) {

			if (StringTokenizer(event.message.contentRaw).countTokens() == 3) {
				//menzionato e parametri a 3

				loadAndPlay(guildMusicManager, event.channel, getUrl(event), false)
			} else {
				event.channel.sendMessage("Quantità di parametri non conformi").queue()
			}
		} else {
			if (StringTokenizer(event.message.contentRaw).countTokens() == 2) {

				loadAndPlay(guildMusicManager, event.channel, getUrl(event), false)
			} else {
				event.channel.sendMessage("Quantità di parametri non conformi").queue()
			}
		}
	}

	/**
	 * Metodo per riprodurre una playlist dall'url passato come parametro
	 * @param event
	 */
	internal fun playPlaylist(event: MessageReceivedEvent) {
		val guildMusicManager = getMusicManager(event.guild.id)
		val guild = event.guild
		if (guild.audioManager.sendingHandler == null) {
			this.join(event)
		}

		loadAndPlay(guildMusicManager, event.channel, getUrl(event), true)
	}

	/**
	 * Metodo per saltare la traccia attualmente in riproduzione
	 * @param event
	 */
	internal fun skip(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)
		guildMusicManager.scheduler.nextTrack()
		event.channel.sendMessage("Canzone saltata").queue()
	}


	/**
	 * Metodo per mettere in pausa o riprendere la traccia attualmente
	 * in riproduzione
	 * @param event
	 */
	internal fun pauseResume(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)
		val player = guildMusicManager.player

		if (player.playingTrack == null) {
			event.channel.sendMessage("Non è possibile mettere in pausa o riprendere il nulla, coglione.").queue()
			return
		}

		player.isPaused = !player.isPaused
		if (player.isPaused)
			event.channel.sendMessage("Riproduzione in pausa").queue()
		else
			event.channel.sendMessage("Ripresa riproduzione").queue()
	}

	/**
	 * Metodo per stoppare la riproduzione e pulire la coda
	 * @param event
	 */
	internal fun stop(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)

		guildMusicManager.scheduler.queue.clear()
		guildMusicManager.player.stopTrack()
		guildMusicManager.player.isPaused = false
		event.channel.sendMessage("Riproduzione stoppata e playlist svuotata").queue()

	}


	/**
	 * Metodo per cambiare il volume, attualmente non in uso.
	 * Bisogno di calcolo computazionale extra per il cambio del volume.
	 * @param event
	 */
	private fun changeVolume(event: MessageReceivedEvent) {
		/*		if (command.length == 1) {
			event.getChannel().sendMessage("Current player volume: **" + player.getVolume() + "**").queue();
		} else {
			try {
				int newVolume = Math.max(10, Math.min(100, Integer.parseInt(command[1])));
				int oldVolume = player.getVolume();
				player.setVolume(newVolume);
				event.getChannel().sendMessage("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`").queue();
			} catch (NumberFormatException e) {
				event.getChannel().sendMessage("`" + command[1] + "` is not a valid integer. (10 - 100)").queue();
			}
		}*/
	}

	/**
	 * Metodo per riavviare la traccia attualmente in riproduzione
	 * @param event
	 */
	internal fun restart(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)

		var track = guildMusicManager.player.playingTrack
		if (track == null)
			track = guildMusicManager.scheduler.lastTrack

		if (track != null) {
			event.channel.sendMessage("Riavvio traccia: " + track.info.title).queue()
			guildMusicManager.player.playTrack(track.makeClone())
		}
		//		else {
		//			//Nessuna traccia è stata pre
		//			event.getChannel().sendMessage("No track has been previously started, so the player cannot replay a track!").queue();
		//		}
	}

	/**
	 * Metodo per ripetere la traccia attualmente in riproduzione
	 * @param event
	 */
	internal fun repeat(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)
		guildMusicManager.scheduler.setRepeating(!guildMusicManager.scheduler.isRepeating())
		event.channel.sendMessage("Riproduzione impostata su: **" + (if (guildMusicManager.scheduler.isRepeating()) "ripeti" else "non ripetere") + "**").queue()

	}

	/**
	 * Metodo per resettare il bot in caso di problemi.
	 * Pulisce la coda, e resetta i canali audio
	 * @param event
	 */
	internal fun reset(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)

		synchronized(musicManagers) {
			guildMusicManager.scheduler.queue.clear()
			guildMusicManager.player.destroy()
			event.guild.audioManager.sendingHandler = null
			musicManagers.remove(event.guild.id)
		}

		event.guild.audioManager.sendingHandler = guildMusicManager.sendHandler
		event.channel.sendMessage("Riproduttore resettato").queue()
	}

	/**
	 * Metodo per ottenere la traccia attualmente in riproduzione
	 * @param event
	 */
	internal fun nowPlay(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)

		val currentTrack = guildMusicManager.player.getPlayingTrack()
		if (currentTrack != null) {
			val title = currentTrack.info.title
			val position = getTimestamp(currentTrack.position)
			val duration = getTimestamp(currentTrack.duration)

			val nowplaying = String.format("**In Riprodzione:** %s\n**Time:** [%s / %s]",
					title, position, duration)

			event.channel.sendMessage(nowplaying).queue()
		} else
			event.channel.sendMessage("Non sto riproducendo nulla!").queue()
	}

	/**
	 * Metodo per ottenere la lista della coda
	 * @param event
	 */
	internal fun getListQueue(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)
		val queue = guildMusicManager.scheduler.queue

		synchronized(queue) {
			if (queue.isEmpty()) {
				event.channel.sendMessage("La coda è vuota").queue()
			} else {
				var trackCount = 0
				var queueLength: Long = 0
				val sb = StringBuilder()
				sb.append("Coda attuale: ").append(queue.size).append("\n")
				for (track in queue) {
					queueLength += track.duration
					if (trackCount < 10) {
						sb.append("`[").append(getTimestamp(track.duration)).append("]` ")
						sb.append(track.info.title).append("\n")
						trackCount++
					}
				}
				sb.append("\n").append("Tempo totale di coda: ").append(getTimestamp(queueLength))

				event.channel.sendMessage(sb.toString()).queue()
			}
		}
	}

	/**
	 * Metodo per randomizzare la coda
	 * @param event
	 */
	internal fun shuffleQueue(event: MessageReceivedEvent) {

		val guildMusicManager = getMusicManager(event.guild.id)

		if (guildMusicManager.scheduler.queue.isEmpty()) {
			event.channel.sendMessage("La coda è attualmente vuota!").queue()
			return
		}

		guildMusicManager.scheduler.shuffle()
		event.channel.sendMessage("Coda mischiata").queue()
	}

	internal fun clearQueue(event: MessageReceivedEvent) {
		val guildMusicManager = getMusicManager(event.guild.id)

		if (!guildMusicManager.scheduler.queue.isEmpty()) {
			guildMusicManager.scheduler.queue.clear()
			event.channel.sendMessage("Coda Pulita").queue()
		} else {
			event.channel.sendMessage("La coda è già vuota").queue()
		}

	}

	/**
	 * Metodo per caricare una traccia o una playlist nella coda
	 * @param mng
	 * @param channel
	 * @param url
	 * @param addPlaylist
	 */
	private fun loadAndPlay(mng: GuildMusicManager, channel: MessageChannel, url: String, addPlaylist: Boolean) {
		val trackUrl: String

		//Strip <>'s that prevent discord from embedding link resources
		if (url.startsWith("<") && url.endsWith(">"))
			trackUrl = url.substring(1, url.length - 1)
		else
			trackUrl = url

		playerManager.loadItemOrdered(mng, trackUrl, object : AudioLoadResultHandler {
			override fun trackLoaded(track: AudioTrack) {
				var msg = "Aggiunta alla coda: " + track.info.title
				if (mng.player.playingTrack == null)
					msg += "\n inizio riproduzione"

				mng.scheduler.queue(track)
				channel.sendMessage(msg).queue()
			}

			override fun playlistLoaded(playlist: AudioPlaylist) {
				var firstTrack: AudioTrack? = playlist.selectedTrack
				val tracks = playlist.tracks


				if (firstTrack == null) {
					firstTrack = playlist.tracks[0]
				}

				if (addPlaylist) {
					channel.sendMessage("Aggiunte **" + playlist.tracks.size + "** tracce alla coda").queue()
//					tracks.forEach(Consumer<AudioTrack> { mng.scheduler.queue() })
					tracks.forEach { _ -> Consumer<AudioTrack> { mng.scheduler.queue } }
				} else {
					channel.sendMessage("Aggiunto alla coda " + firstTrack!!.info.title).queue()
					mng.scheduler.queue(firstTrack)
				}
			}

			override fun noMatches() {
				channel.sendMessage("Non ho abbastanza forza per vedere dentro a $trackUrl").queue()
			}

			override fun loadFailed(exception: FriendlyException) {
				channel.sendMessage("Non è nel mio potere: " + exception.message).queue()
			}
		})
	}

	private fun getMusicManager(guildId: String): GuildMusicManager {
		val mng: GuildMusicManager? = musicManagers[guildId]

		if (mng == null) {
			val musicMan: GuildMusicManager
			synchronized(musicManagers) {
				musicMan = GuildMusicManager(playerManager)
				musicMan.player.volume = DEFAULT_VOLUME
				musicManagers.put(guildId, musicMan)
			}
			return musicMan
		} else {
			return mng
		}
	}


}