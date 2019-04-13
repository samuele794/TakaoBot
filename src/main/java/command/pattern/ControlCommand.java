package command.pattern;

import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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

	public static String getSimbolCommand(String serverId) {
		return PostgreSQLInterface.getSimbol(serverId);
	}

	public static boolean checkCommand(MessageReceivedEvent event, String simbolCommand, String commandName) {
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
