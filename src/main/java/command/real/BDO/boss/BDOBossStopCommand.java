package command.real.BDO.boss;

import beans.ServerToChannel;
import command.pattern.ControlCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

public class BDOBossStopCommand extends ListenerAdapter {

	public static String getCommand() {
		return "BDOBossStop";
	}

	public static String getCommandDescription() {
		return "Questo comando permette disiscriversi agli allarmi dei boss di BDO. \n" +
				"Il comando può essere lanciato su qualunque canale";
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
				ArrayList<ServerToChannel> listChannel = SQLiteInterfaces.getBDOBossChannel();
				ServerToChannel removedChannelId = listChannel.get(listChannel.indexOf(new ServerToChannel(event.getGuild().getId(), null)));

				SQLiteInterfaces.removeBDOBossChannel(event.getGuild().getId());

				new MessageBuilder().append("Invio degli allarmi dei boss di BDO rimosso dal canale: ")
						.appendCodeBlock(event.getJDA().getTextChannelById(removedChannelId.getChannelID()).getName(), "")
						.sendTo(event.getChannel()).queue();
			}
		}
	}
}