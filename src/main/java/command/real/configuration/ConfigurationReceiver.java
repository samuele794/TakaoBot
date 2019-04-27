package command.real.configuration;

import command.pattern.ControlCommand;
import command.real.BDO.BDOReceiver;
import command.real.tpl.atmAlert.ATMAlertReceiver;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static starter.Start.jda;

public class ConfigurationReceiver extends ListenerAdapter {

	public static String getConfigurationCommandCommand() {
		return "configurationCommand";
	}

	public static String getConfigurationCommandCommandDescription() {
		return "Questo comando permette la configurazione del prefisso per i comandi \n" +
				"Questo comando è riservato al solo utilizzo da parte del proprietario del server.";
	}

	public static String getInfoCommand() {
		return "info";
	}

	public static String getInfoCommandDescription() {
		return "Info del bot.";
	}

	public String getHelpCommand() {
		return "help";
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		if (event.getAuthor().isBot()) return;
		if (event.isFromType(ChannelType.PRIVATE)) {
			return;
		}

		String simbolCommand = ControlCommand.getSimbolCommand(event.getGuild().getId());

		if (ControlCommand.checkCommand(event, simbolCommand, getConfigurationCommandCommand())) {

			if (!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {
				if (event.getMessage().isMentioned(event.getJDA().getSelfUser())) {

					if ((new StringTokenizer(event.getMessage().getContentRaw())).countTokens() == 3) {
						//menzionato e parametri a 3
						configuration(event);
					} else {
						event.getChannel().sendMessage("Quantità di parametri non conformi").queue();
					}
				} else {
					if ((new StringTokenizer(event.getMessage().getContentRaw())).countTokens() == 2) {
						configuration(event);

					} else {
						event.getChannel().sendMessage("Quantità di parametri non conformi").queue();
					}
				}
			}


		}

		if (ControlCommand.checkCommand(event, simbolCommand, getHelpCommand())) {

			EmbedBuilder builder = new EmbedBuilder();

			builder.setTitle("Lista Comandi").setColor(new Color(132, 197, 251))
					.setDescription("Questi comandi possono essere usati menzionando il bot e scrivendo il comando senza simbolo," +
							"oppure usando i comandi direttamente come segue:")

					.addField(simbolCommand + ConfigurationReceiver.getInfoCommand(), ConfigurationReceiver.getInfoCommandDescription(), false)
					.addField(simbolCommand + ConfigurationReceiver.getConfigurationCommandCommand(), ConfigurationReceiver.getConfigurationCommandCommandDescription(), false)
					.addField(simbolCommand + BDOReceiver.getBDONewsStartCommand(), BDOReceiver.getBDONewsStartCommandDecription(), false)
					.addField(simbolCommand + BDOReceiver.getBDONewsStopCommand(), BDOReceiver.getBDONewsStopCommandDecription(), false)
					.addField(simbolCommand + BDOReceiver.getBDOPatchStartCommand(), BDOReceiver.getBDOPatchStartCommandDecription(), false)
					.addField(simbolCommand + BDOReceiver.getBDOPatchStopCommand(), BDOReceiver.getBDOPatchStopCommandDecription(), false)
					.addField(simbolCommand + BDOReceiver.getBDOBossStartCommand(), BDOReceiver.getBDOBossStartCommandDescription(), false)
					.addField(simbolCommand + BDOReceiver.getBDOBossStopCommand(), BDOReceiver.getBDOBossStopCommandDescription(), false)
					.addField(simbolCommand + ATMAlertReceiver.getATMStartCommand(), ATMAlertReceiver.getATMStartCommandDescription(), false)
					.addField(simbolCommand + ATMAlertReceiver.getATMStopCommand(), ATMAlertReceiver.getATMStopCommandDescription(), false);

			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(builder.build()).queue();
			});

		}

		if (ControlCommand.checkCommand(event, simbolCommand, getInfoCommand())) {

			EmbedBuilder builder = new EmbedBuilder();

			String info = "Ciao, sono samuele794#8585 lo sviluppatore di questo bot, scritto Java 8 con l'utilizzo " +
					"della libreria di base JDA (Java Discord Api). \n\n" +
					"Il personaggio Takao di Arpeggio Of Blue Steel (\u84bc\u304d\u92fc\u306e\u30a2\u30eb\u30da\u30b8\u30aa, Aoki Hagane no Arpeggio) " +
					"è stato creato da Ark Performance. \nNon sono il possessore di nessuna immagine " +
					"utilizzata all'interno del bot. \n\n" +
					"Se sei uno sviluppatore Java e vuoi contribuire al bot sei ben accetto. \n" +
					"Link GitHub: https://github.com/samuele794/TakaoBot \n\n" +
					"Usando questo bot accetti il fatto che conserverò l'uso dei comandi per fini della manutenzione del bot \n\n" +
					"Link utili: \n" +
					"- Sito web: https://samuele794.github.io/TakaoBot/";

			String avatarUrl = jda.getUserById("186582756841488385").getAvatarUrl();

			MessageEmbed message = builder
					.setImage("https://samuele794.github.io/TakaoBot/images/Copertina.png")
					.setThumbnail(avatarUrl)
					.setDescription(info)
					.setColor(new Color(131, 196, 250))
					.build();

			event.getAuthor().openPrivateChannel().queue(privateChannel -> {
				privateChannel.sendMessage(message).queue();
			});


		}


	}

	private void configuration(MessageReceivedEvent event) {
		if (!(event.getMessage().getContentRaw().contains("\"") | event.getMessage().getContentRaw().contains("\\") |
				event.getMessage().getContentRaw().contains("'"))) {

			List<String> listMessage = Arrays.asList(event.getMessage().getContentRaw().split(" "));
			String newCommand = listMessage.get(listMessage.size() - 1);
			PostgreSQLInterface.setSimbol(newCommand, event.getGuild().getId());

			new MessageBuilder().append("Simbolo di comando configurato. Nuovo simbolo di comando: ")
					.appendCodeBlock(PostgreSQLInterface.getSimbol(event.getGuild().getId()), "").sendTo(event.getChannel()).queue();
		} else {
			new MessageBuilder().append("Simbolo di comando non conforme").sendTo(event.getChannel()).queue();
		}
	}
}
