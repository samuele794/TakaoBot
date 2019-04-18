package command.real.tpl.atmAlert;

import beans.ServerToChannel;
import command.pattern.ControlCommand;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

@Deprecated
public class ATMAlertStopCommand extends ListenerAdapter {

	public static String getCommand() {
		return "ATMAlertStop";
	}

	public static String getCommandDescription() {
		return "Questo comando permette disiscriversi agli avvisi dell'ATM Milano. \n" +
				"Il comando può essere lanciato su qualunque canale";
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		if (ControlCommand.controlCommand(event, getCommand())) {

			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {
				ArrayList<ServerToChannel> listChannel = PostgreSQLInterface.getATMAlertChannel();
				ServerToChannel removedChannelId = listChannel.get(listChannel.indexOf(new ServerToChannel(event.getGuild().getId(), null)));

				PostgreSQLInterface.removeATMAlertChannel(event.getGuild().getId());

				new MessageBuilder().append("Invio degli avvisi dell'ATM rimosso dal canale: ")
						.appendCodeBlock(event.getJDA().getTextChannelById(removedChannelId.getChannelID()).getName(), "")
						.sendTo(event.getChannel()).queue();
			}
		}
	}
}