package command.pattern;

import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ControlCommand {
    /**
     * Metodo per eseguire il controllo
     *
     * @param event
     * @param commandName
     * @return
     */
    public static boolean controlCommand(MessageReceivedEvent event, String commandName) {

        if (!event.isFromType(ChannelType.PRIVATE)) {
            if ((event.getMessage().getContentRaw().startsWith(SQLiteInterfaces.getSimbol(event.getGuild().getId()) + commandName)
                    || (event.getMessage().isMentioned(event.getJDA().getSelfUser()) && event.getMessage().getContentRaw().contains(commandName)))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
