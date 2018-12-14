package command.real.configuration;

import command.pattern.ControlCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;

public class ConfigurationCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        var authorID = event.getAuthor().getId();
        var ownerID = event.getGuild().getOwnerId();

        if (!ownerID.equals(authorID)) return;

        if (ControlCommand.controlCommand(event, "configurationCommand")) {

            if (!(event.getMessage().getContentRaw().contains("\"") | event.getMessage().getContentRaw().contains("\\"))) {

                List<String> listMessage = Arrays.asList(event.getMessage().getContentRaw().split(" "));
                var newCommand = listMessage.get(listMessage.size() - 1);
                SQLiteInterfaces.setSimbol(newCommand, event.getGuild().getId());

                new MessageBuilder().append("Simbolo di comando configurato. Nuovo simbolo di comando: ")
                        .appendCodeBlock(SQLiteInterfaces.getSimbol(event.getGuild().getId()), "").sendTo(event.getChannel()).queue();
            } else {
                new MessageBuilder().append("Simbolo di comando non conforme").sendTo(event.getChannel()).queue();
            }

        }
    }
}
