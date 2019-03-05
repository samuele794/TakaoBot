package command.real.BDO.RSS;

import beans.ServerToChannel;
import command.pattern.ControlCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

public class BDONewsStopCommand extends ListenerAdapter {

    public static String getCommand() {
        return "BDONewsStop";
    }

    public static String getCommandDescription() {
        return "Questo comando permette disiscriversi al feed delle news di BDO. \n" +
                "Il comando può essere lanciato su qualunque canale";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (ControlCommand.controlCommand(event, getCommand())) {
            String authorID = event.getAuthor().getId();
            String ownerID = event.getGuild().getOwnerId();

            if (!ownerID.equals(authorID)) {
                event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
            } else {

                ArrayList<ServerToChannel> listChannel = SQLiteInterfaces.getBDONewsChannel();
                ServerToChannel removedChannelId = listChannel.get(listChannel.indexOf(new ServerToChannel(event.getGuild().getId(), null)));

                SQLiteInterfaces.removeBDONewsChannel(event.getGuild().getId());

                new MessageBuilder().append("Invio delle news di BDO rimosso dal canale: ")
                        .appendCodeBlock(event.getJDA().getTextChannelById(removedChannelId.getChannelID()).getName(), "")
                        .sendTo(event.getChannel()).queue();
            }
        }
    }


}
