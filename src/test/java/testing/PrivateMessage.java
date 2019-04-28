package testing;

import command.pattern.ControlCommand;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class PrivateMessage extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (event.getAuthor().isBot()) return;

        if (ControlCommand.controlCommand(event,"tt")){
            User user = event.getAuthor();
            user.openPrivateChannel().queue((privateChannel -> {
                MessageBuilder builder = new MessageBuilder();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.MAGENTA)
                        .setAuthor(user.getName(), null, user.getAvatarUrl())
                        .setTitle("Ciao " + event.getAuthor().getName())
                        .setDescription("Il tuo simbolo di comando è: " + PostgreSQLInterface.getSimbol(event.getGuild().getId()));
                privateChannel.sendMessage(embedBuilder.build()).queue();


            }));
            event.getMessage().getChannel().deleteMessageById(event.getMessageId()).queue();
        }
    }
}
