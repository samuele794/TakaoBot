package command.real.BDO;

import beans.ServerToChannel;
import command.pattern.ControlCommand;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

public class BDOReceiver extends ListenerAdapter {

	//BDO BOSS START
	public static String getBDOBossStartCommand() {
		return "BDOBossStart";
	}

	public static String getBDOBossStartCommandDescription() {
		return "Questo comando permette di iscriversi agli allarmi dei boss di BDO. \n" +
				"Il comando deve essere lanciato sul canale su cui si desidera ricevere i boss";
	}

	//BDO BOSS STOP

	public static String getBDOBossStopCommand() {
		return "BDOBossStop";
	}

	public static String getBDOBossStopCommandDescription() {
		return "Questo comando permette disiscriversi agli allarmi dei boss di BDO. \n" +
				"Il comando può essere lanciato su qualunque canale";
	}

	//BDO NEWS START

	public static String getBDONewsStartCommand() {
		return "BDONewsStart";
	}

	public static String getBDONewsStartCommandDecription() {
		return "Questo comando permette di iscriversi al feed delle news di BDO. \n" +
				"Il comando deve essere lanciato sul canale su cui si desidera ricevere le news";
	}

	//BDO NEWS STOP

	public static String getBDONewsStopCommand() {
		return "BDONewsStop";
	}

	public static String getBDONewsStopCommandDecription() {
		return "Questo comando permette disiscriversi al feed delle news di BDO. \n" +
				"Il comando può essere lanciato su qualunque canale";
	}

	//BDO PATCH START

	public static String getBDOPatchStartCommand() {
		return "BDOPatchStart";
	}

	public static String getBDOPatchStartCommandDecription() {
		return "Questo comando permette di iscriversi al feed delle patch di BDO. \n" +
				"Il comando deve essere lanciato sul canale su cui si desidera ricevere le patch";
	}

	//BDO PATCH STOP

	public static String getBDOPatchStopCommand() {
		return "BDOPatchStop";
	}

	public static String getBDOPatchStopCommandDecription() {
		return "Comando per disiscriversi al feed delle patch di BDO. \n" +
				"Il comando può essere lanciato su qualunque canale";
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		if (event.getAuthor().isBot()) return;

		String simbolCommand = ControlCommand.getSimbolCommand(event.getGuild().getId());

		if (ControlCommand.checkCommand(event, simbolCommand, getBDOBossStartCommand())) {
			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {
				PostgreSQLInterface.setBDOBossChannel(event.getGuild().getId(), event.getChannel().getId());
				new MessageBuilder().append("Invio degli allarmi dei boss di BDO configurato sul canale: ")
						.appendCodeBlock(event.getChannel().getName(), "").sendTo(event.getChannel()).queue();
			}
		} else if (ControlCommand.checkCommand(event, simbolCommand, getBDOBossStopCommand())) {

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
		} else if (ControlCommand.checkCommand(event, simbolCommand, getBDONewsStartCommand())) {
			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {
				PostgreSQLInterface.setBDONewsChannel(event.getGuild().getId(), event.getChannel().getId());
				new MessageBuilder().append("Invio delle news di BDO configurato sul canale: ")
						.appendCodeBlock(event.getChannel().getName(), "").sendTo(event.getChannel()).queue();

			}
		} else if (ControlCommand.checkCommand(event, simbolCommand, getBDONewsStopCommand())) {
			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {

				ArrayList<ServerToChannel> listChannel = PostgreSQLInterface.getBDONewsChannel();
				ServerToChannel removedChannelId = listChannel.get(listChannel.indexOf(new ServerToChannel(event.getGuild().getId(), null)));

				PostgreSQLInterface.removeBDONewsChannel(event.getGuild().getId());

				new MessageBuilder().append("Invio delle news di BDO rimosso dal canale: ")
						.appendCodeBlock(event.getJDA().getTextChannelById(removedChannelId.getChannelID()).getName(), "")
						.sendTo(event.getChannel()).queue();
			}
		} else if (ControlCommand.checkCommand(event, simbolCommand, getBDOPatchStartCommand())) {
			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {

				PostgreSQLInterface.setBDOPatchChannel(event.getGuild().getId(), event.getChannel().getId());
				new MessageBuilder().append("Invio delle patch di BDO configurato sul canale: ")
						.appendCodeBlock(event.getChannel().getName(), "").sendTo(event.getChannel()).queue();
			}
		} else if (ControlCommand.checkCommand(event, simbolCommand, getBDOPatchStopCommand())) {
			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {

				ArrayList<ServerToChannel> listChannel = PostgreSQLInterface.getBDOPatchChannel();
				ServerToChannel removedChannelId = listChannel.get(listChannel.indexOf(new ServerToChannel(event.getGuild().getId(), null)));

				PostgreSQLInterface.removeBDOPatchChannel(event.getGuild().getId());

				new MessageBuilder().append("Invio delle patch di BDO rimosso dal canale: ")
						.appendCodeBlock(event.getJDA().getTextChannelById(removedChannelId.getChannelID()).getName(), "")
						.sendTo(event.getChannel()).queue();
			}
		}
	}
}
