package command.real.BDO.RSS;

import beans.ServerToChannel;
import command.pattern.ControlCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BDOPatchStopCommand extends ListenerAdapter {

    public static String getCommand() {
        return "BDOPatchStop";
    }

    public static String getCommandDescription() {
        return "Comando per disiscriversi al feed delle patch di BDO. \n" +
                "Il comando può essere lanciato su qualunque canale";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (ControlCommand.controlCommand(event, getCommand())) {

            var listChannel = SQLiteInterfaces.getBDOPatchChannel();
            var removedChannelId = listChannel.get(listChannel.indexOf(new ServerToChannel(event.getGuild().getId(), null)));

            SQLiteInterfaces.removeBDOPatchChannel(event.getGuild().getId());

            new MessageBuilder().append("Invio delle patch di BDO rimosso dal canale: ")
                    .appendCodeBlock(event.getJDA().getTextChannelById(removedChannelId.getChannelID()).getName(), "")
                    .sendTo(event.getChannel()).queue();
        }
    }


}
