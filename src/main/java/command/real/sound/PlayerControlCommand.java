package command.real.sound;

import command.pattern.ControlCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PlayerControlCommand extends ListenerAdapter {

	private MusicManager musicManager;

	public PlayerControlCommand() {
		this.musicManager = new MusicManager();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}

		if (ControlCommand.controlCommand(event, "leave")) {

			musicManager.leave(event);

		} else if (ControlCommand.controlCommand(event, "play")) {

			musicManager.play(event);

		} else if (ControlCommand.controlCommand(event, "playPlaylist") |
				ControlCommand.controlCommand(event, "pp")) {

			musicManager.playPlaylist(event);

		} else if (ControlCommand.controlCommand(event, "skip") |
				ControlCommand.controlCommand(event, "salta")) {

			musicManager.skip(event);

		} else if (ControlCommand.controlCommand(event, "pausa") |
				ControlCommand.controlCommand(event, "pause") |
				ControlCommand.controlCommand(event, "resume") |
				ControlCommand.controlCommand(event, "riprendi")) {

			musicManager.pauseResume(event);

		} else if (ControlCommand.controlCommand(event, "stop")) {

			musicManager.stop(event);

		} else if (ControlCommand.controlCommand(event, "restart")) {

			musicManager.restart(event);

		} else if (ControlCommand.controlCommand(event, "repeat") |
				ControlCommand.controlCommand(event, "ripeti")) {

			musicManager.repeat(event);

		} else if (ControlCommand.controlCommand(event, "reset")) {

			musicManager.reset(event);

		} else if (ControlCommand.controlCommand(event, "nowplaying") |
				ControlCommand.controlCommand(event, "np")) {

			musicManager.nowPlay(event);

		} else if (ControlCommand.controlCommand(event, "queue") |
				ControlCommand.controlCommand(event, "lista")) {

			musicManager.getListQueue(event);

		} else if (ControlCommand.controlCommand(event, "shuffle")) {

			musicManager.shuffleQueue(event);

		} else if (ControlCommand.controlCommand(event, "clear") |
				ControlCommand.controlCommand(event, "pulisci")) {

			musicManager.clearQueue(event);
		}
	}


}