package command.real.configuration;

import command.pattern.ControlCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class InfoCommand extends ListenerAdapter {

    public String getCommand() {
        return "info";
    }

    public String getCommandDescription() {
        return null;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (ControlCommand.controlCommand(event, getCommand())) {

            EmbedBuilder builder = new EmbedBuilder();


        }

    }


}
