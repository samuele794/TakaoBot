package command.real.sound;

import command.pattern.ControlCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.logging.Level;

public class PlayerControlCommand extends ListenerAdapter {

	private MusicManager musicManager;

	public PlayerControlCommand() {
		java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);
		this.musicManager = new MusicManager();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		String[] command = event.getMessage().getContentDisplay().split(" ", 2);


//		if (ControlCommand.controlCommand(event, "join")) {
//
//			musicManager.join(event);
//
//		} else

		if (ControlCommand.controlCommand(event, "leave")) {

			musicManager.leave(event);

		} else if (ControlCommand.controlCommand(event, "play")) {
			//funziona
			musicManager.play(event);

		} else if (ControlCommand.controlCommand(event, "playPlaylist") |
				ControlCommand.controlCommand(event, "pp")) {

			musicManager.playPlaylist(event);

		} else if (ControlCommand.controlCommand(event, "skip") |
				ControlCommand.controlCommand(event, "salta")) {

			musicManager.skip(event);

		} else if (ControlCommand.controlCommand(event, "pausa") |
				ControlCommand.controlCommand(event, "pauseResume") |
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
		}
	}


}