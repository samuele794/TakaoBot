package command.real.BDO.RSS;

import command.pattern.CommandDescriptor;
import command.pattern.ControlCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BDOPatchStartCommand extends ListenerAdapter implements CommandDescriptor {

    @Override
    public String getCommand() {
        return "BDOPatchStart";
    }

    @Override
    public String getCommandDescription() {
        return "Comando per iscriversi al feed delle patch di BDO. \n" +
                "Il comando deve essere lanciato sul canale su cui si desidera ricevere le patch";
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (ControlCommand.controlCommand(event, getCommand())) {
            SQLiteInterfaces.setBDOPatchChannel(event.getGuild().getId(),event.getChannel().getId());
            new MessageBuilder().append("Invio delle patch di BDO configurato sul canale: ")
                    .appendCodeBlock(event.getChannel().getName(),"").sendTo(event.getChannel()).queue();

        }
    }
}
