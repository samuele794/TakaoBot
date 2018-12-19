package command.real.configuration;

import command.pattern.ControlCommand;
import command.real.BDO.RSS.BDONewsStartCommand;
import command.real.BDO.RSS.BDONewsStopCommand;
import command.real.BDO.RSS.BDOPatchStartCommand;
import command.real.BDO.RSS.BDOPatchStopCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {

    public String getCommand() {
        return "help";
    }

    public String getCommandDescription() {
        return null;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;


        if (ControlCommand.controlCommand(event, getCommand())) {

            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("Lista Comandi").addField(ConfigurationCommand.getCommand(), ConfigurationCommand.getCommandDescription(), false)
                    .addField(BDONewsStartCommand.getCommand(), BDONewsStartCommand.getCommandDescription(), false)
                    .addField(BDONewsStopCommand.getCommand(), BDONewsStopCommand.getCommandDescription(), false)
                    .addField(BDOPatchStartCommand.getCommand(), BDOPatchStartCommand.getCommandDescription(), false)
                    .addField(BDOPatchStopCommand.getCommand(), BDOPatchStopCommand.getCommandDescription(), false);

            event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(builder.build()).queue();
            });

        }

    }


}
