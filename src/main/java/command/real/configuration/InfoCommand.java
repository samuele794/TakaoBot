package command.real.configuration;

import command.pattern.ControlCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

import static starter.Start.jda;

public class InfoCommand extends ListenerAdapter {

	public static String getCommand() {
		return "info";
	}

	public static String getCommandDescription() {
		return "Info del bot.";
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		if (ControlCommand.controlCommand(event, getCommand())) {

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


}
