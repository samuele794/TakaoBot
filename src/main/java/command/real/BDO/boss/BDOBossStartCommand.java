package command.real.BDO.boss;

import command.pattern.ControlCommand;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Deprecated
public class BDOBossStartCommand extends ListenerAdapter {

	public static String getCommand() {
		return "BDOBossStart";
	}

	public static String getCommandDescription() {
		return "Questo comando permette di iscriversi agli allarmi dei boss di BDO. \n" +
				"Il comando deve essere lanciato sul canale su cui si desidera ricevere i boss";
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		if (ControlCommand.controlCommand(event, getCommand())) {

			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {
				PostgreSQLInterface.setBDOBossChannel(event.getGuild().getId(), event.getChannel().getId());
				new MessageBuilder().append("Invio degli allarmi dei boss di BDO configurato sul canale: ")
						.appendCodeBlock(event.getChannel().getName(), "").sendTo(event.getChannel()).queue();
			}
		}
	}
}
