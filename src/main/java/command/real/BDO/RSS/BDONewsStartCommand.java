package command.real.BDO.RSS;

import command.pattern.ControlCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BDONewsStartCommand extends ListenerAdapter {

	public static String getCommand() {
		return "BDONewsStart";
	}

	public static String getCommandDescription() {
		return "Questo comando permette di iscriversi al feed delle news di BDO. \n" +
				"Il comando deve essere lanciato sul canale su cui si desidera ricevere le news";
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		if (ControlCommand.controlCommand(event, getCommand())) {
			String authorID = event.getAuthor().getId();
			String ownerID = event.getGuild().getOwnerId();

			if (!ownerID.equals(authorID)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {
				SQLiteInterfaces.setBDONewsChannel(event.getGuild().getId(), event.getChannel().getId());
				new MessageBuilder().append("Invio delle news di BDO configurato sul canale: ")
						.appendCodeBlock(event.getChannel().getName(), "").sendTo(event.getChannel()).queue();

			}
		}
	}


}
