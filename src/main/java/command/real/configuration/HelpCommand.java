package command.real.configuration;

import command.pattern.ControlCommand;
import command.real.BDO.BDOReceiver;
import command.real.tpl.atmAlert.ATMAlertReceiver;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

@Deprecated
public class HelpCommand extends ListenerAdapter {

	public String getCommand() {
		return "help";
	}

	public String getCommandDescription() {
		return null;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;


		if (ControlCommand.controlCommand(event, getCommand())) {

			String simbol = PostgreSQLInterface.getSimbol(event.getGuild().getId());
			EmbedBuilder builder = new EmbedBuilder();

			builder.setTitle("Lista Comandi").setColor(new Color(132, 197, 251))
					.setDescription("Questi comandi possono essere usati menzionando il bot e scrivendo il comando senza simbolo," +
							"oppure usando i comandi direttamente come segue:")

					.addField(simbol + InfoCommand.getCommand(), InfoCommand.getCommandDescription(), false)
					.addField(simbol + ConfigurationCommand.getCommand(), ConfigurationCommand.getCommandDescription(), false)
					.addField(simbol + BDOReceiver.getBDONewsStartCommand(), BDOReceiver.getBDONewsStartCommandDecription(), false)
					.addField(simbol + BDOReceiver.getBDONewsStopCommand(), BDOReceiver.getBDONewsStopCommandDecription(), false)
					.addField(simbol + BDOReceiver.getBDOPatchStartCommand(), BDOReceiver.getBDOPatchStartCommandDecription(), false)
					.addField(simbol + BDOReceiver.getBDOPatchStopCommand(), BDOReceiver.getBDOPatchStopCommandDecription(), false)
					.addField(simbol + BDOReceiver.getBDOBossStartCommand(), BDOReceiver.getBDOBossStartCommandDescription(), false)
					.addField(simbol + BDOReceiver.getBDOBossStopCommand(), BDOReceiver.getBDOBossStopCommandDescription(), false)
					.addField(simbol + ATMAlertReceiver.getATMStartCommand(), ATMAlertReceiver.getATMStartCommandDescription(), false)
					.addField(simbol + ATMAlertReceiver.getATMStopCommand(), ATMAlertReceiver.getATMStopCommandDescription(), false);

			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});

		}

	}


}
