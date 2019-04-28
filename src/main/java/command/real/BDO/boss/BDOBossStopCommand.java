package command.real.BDO.boss;

import beans.ServerToChannel;
import command.pattern.ControlCommand;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

@Deprecated
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

			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {
				ArrayList<ServerToChannel> listChannel = PostgreSQLInterface.getBDOBossChannel();
				ServerToChannel removedChannelId = listChannel.get(listChannel.indexOf(new ServerToChannel(event.getGuild().getId(), null)));

				PostgreSQLInterface.removeBDOBossChannel(event.getGuild().getId());

				new MessageBuilder().append("Invio degli allarmi dei boss di BDO rimosso dal canale: ")
						.appendCodeBlock(event.getJDA().getTextChannelById(removedChannelId.getChannelID()).getName(), "")
						.sendTo(event.getChannel()).queue();
			}
		}
	}
}
