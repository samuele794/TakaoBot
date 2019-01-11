package command.real.configuration;

import command.pattern.ControlCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ConfigurationCommand extends ListenerAdapter {

    public static String getCommand() {
        return "configurationCommand";
    }

    public static String getCommandDescription() {
        return "Questo comando permette la configurazione del prefisso per i comandi \n" +
                "Questo comando è riservato al solo utilizzo da parte del proprietario del server.";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (event.isFromType(ChannelType.PRIVATE)) {
            return;
        }

        String authorID = event.getAuthor().getId();
        String ownerID = event.getGuild().getOwnerId();

        if (!ownerID.equals(authorID)) {
            event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
            return;
        }

        if (ControlCommand.controlCommand(event, getCommand())) {

            if (event.getMessage().isMentioned(event.getJDA().getSelfUser()) & (new StringTokenizer(event.getMessage().getContentRaw())).countTokens() == 3) {
                //menzionato e parametri a 3

                configuration(event);
            } else {
                if ((new StringTokenizer(event.getMessage().getContentRaw())).countTokens() == 2) {
                    configuration(event);

                } else {
                    event.getChannel().sendMessage("Quantità di parametri non conformi").queue();
                }
            }
        }
    }

    private void configuration(MessageReceivedEvent event) {
        if (!(event.getMessage().getContentRaw().contains("\"") | event.getMessage().getContentRaw().contains("\\"))) {

            List<String> listMessage = Arrays.asList(event.getMessage().getContentRaw().split(" "));
            String newCommand = listMessage.get(listMessage.size() - 1);
            SQLiteInterfaces.setSimbol(newCommand, event.getGuild().getId());

            new MessageBuilder().append("Simbolo di comando configurato. Nuovo simbolo di comando: ")
                    .appendCodeBlock(SQLiteInterfaces.getSimbol(event.getGuild().getId()), "").sendTo(event.getChannel()).queue();
        } else {
            new MessageBuilder().append("Simbolo di comando non conforme").sendTo(event.getChannel()).queue();
        }
    }

}