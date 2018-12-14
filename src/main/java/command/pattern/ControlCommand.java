package command.pattern;

import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.nio.charset.StandardCharsets;

public class ControlCommand {
    public static boolean controlCommand(MessageReceivedEvent event, String commandName){

        if ((event.getMessage().getContentRaw().startsWith(SQLiteInterfaces.getSimbol(event.getGuild().getId()) + commandName)
                || (event.getMessage().isMentioned(event.getJDA().getSelfUser()) && event.getMessage().getContentRaw().contains(commandName)))){
            return true;
        }else{
            return false;
        }
    }
}
