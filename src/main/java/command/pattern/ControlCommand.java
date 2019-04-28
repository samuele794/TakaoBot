package command.pattern;

import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Classe per gestire i controlli tra i simbolo di comanndo e il comando stesso
 */
public class ControlCommand {
	/**
	 * Metodo per eseguire il controllo se il comando inserito è quello di controllo
	 *
	 * @param event       evento del messaggio
	 * @param commandName nome del comando
	 * @return boolean il comando corrisponde a quello ricevuto con quello controllato
	 */

	@Deprecated
	public static boolean controlCommand(MessageReceivedEvent event, String commandName) {

//        System.out.println("Comando inviato da server: " + event.getGuild().getName());
//        System.out.println("Inviato da: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator());

//        LOGGER.log(Level.ALL,"Comando inviato da server: " + event.getGuild().getName());
//        LOGGER.debug("Inviato da: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator());

		if (!event.isFromType(ChannelType.PRIVATE)) {
			if (event.getMessage().getContentRaw().startsWith(PostgreSQLInterface.getSimbol(event.getGuild().getId()) + commandName)
					|| (event.getMessage().isMentioned(event.getJDA().getSelfUser()) && event.getMessage().getContentRaw().contains(commandName))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Metodo per ottere il simbolo di comando del server
	 *
	 * @param serverId ID del server
	 * @return String simbolo di comando
	 */
	public static String getSimbolCommand(String serverId) {
		return PostgreSQLInterface.getSimbol(serverId);
	}

	/**
	 * Controllo del comando lanciato dalla chat se sia corretto con quelli attivi sul bot.
	 * Combaciando il simbolo di comando con il nome del comando oppure con la menzione del bot, seguito dal nome del comando.
	 *
	 * @param event         Evento del messaggio
	 * @param simbolCommand Simbolo di comando
	 * @param commandName   Nome del comando
	 * @return boolean Validità del comando
	 */
	public static boolean checkCommand(@NotNull MessageReceivedEvent event, @NotNull String simbolCommand, @NotNull String commandName) {
		String completeCommand = simbolCommand + commandName.toLowerCase();

		if (!event.isFromType(ChannelType.PRIVATE)) {
			if (event.getMessage().getContentRaw().equals(completeCommand) ||
					(event.getMessage().isMentioned(event.getJDA().getSelfUser()) && event.getMessage().getContentRaw().contains(commandName))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
