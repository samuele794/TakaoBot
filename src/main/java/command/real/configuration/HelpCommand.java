package command.real.configuration;

import command.pattern.ControlCommand;
import command.real.BDO.RSS.BDONewsStartCommand;
import command.real.BDO.RSS.BDONewsStopCommand;
import command.real.BDO.RSS.BDOPatchStartCommand;
import command.real.BDO.RSS.BDOPatchStopCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

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

            String simbol = SQLiteInterfaces.getSimbol(event.getGuild().getId());
            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("Lista Comandi").setColor(new Color(132, 197, 251))
                    .setDescription("Questi comandi possono essere usati menzionando il bot e scrivendo il comando,")

                    .addField(simbol + ConfigurationCommand.getCommand(), ConfigurationCommand.getCommandDescription(), false)
                    .addField(simbol + BDONewsStartCommand.getCommand(), BDONewsStartCommand.getCommandDescription(), false)
                    .addField(simbol + BDONewsStopCommand.getCommand(), BDONewsStopCommand.getCommandDescription(), false)
                    .addField(simbol + BDOPatchStartCommand.getCommand(), BDOPatchStartCommand.getCommandDescription(), false)
                    .addField(simbol + BDOPatchStopCommand.getCommand(), BDOPatchStopCommand.getCommandDescription(), false);

            event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(builder.build()).queue();
            });

        }

    }


}
