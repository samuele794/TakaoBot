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
		if (event.getAuthor().isBot()) return;

		String simbolCommand = ControlCommand.getSimbolCommand(event.getGuild().getId());


		if (ControlCommand.checkCommand(event, simbolCommand, "leave")) {

			musicManager.leave(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "play")) {

			musicManager.play(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "playPlaylist") |
				ControlCommand.checkCommand(event, simbolCommand, "pp")) {

			musicManager.playPlaylist(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "skip") |
				ControlCommand.checkCommand(event, simbolCommand, "salta")) {

			musicManager.skip(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "pausa") |
				ControlCommand.checkCommand(event, simbolCommand, "pause") |
				ControlCommand.checkCommand(event, simbolCommand, "resume") |
				ControlCommand.checkCommand(event, simbolCommand, "riprendi")) {

			musicManager.pauseResume(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "stop")) {

			musicManager.stop(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "restart")) {

			musicManager.restart(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "repeat") |
				ControlCommand.checkCommand(event, simbolCommand, "ripeti")) {

			musicManager.repeat(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "reset")) {

			musicManager.reset(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "nowplaying") |
				ControlCommand.checkCommand(event, simbolCommand, "np")) {

			musicManager.nowPlay(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "queue") |
				ControlCommand.checkCommand(event, simbolCommand, "lista")) {

			musicManager.getListQueue(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "shuffle")) {

			musicManager.shuffleQueue(event);

		} else if (ControlCommand.checkCommand(event, simbolCommand, "clear") |
				ControlCommand.checkCommand(event, simbolCommand, "pulisci")) {

			musicManager.clearQueue(event);
		}
	}


}