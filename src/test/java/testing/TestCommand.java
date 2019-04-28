package testing;

import command.pattern.ControlCommand;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class TestCommand extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		// We don't want to respond to other bot accounts, including ourself
//		Message message = event.getMessage();
//		String content = message.getContentRaw();
		// getContentRaw() is an atomic getter
		// getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
        /*if (content.equals("!ping")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
        }*/


		if (ControlCommand.controlCommand(event, "ping")) {

			EmbedBuilder messageEmbed = new EmbedBuilder();

			messageEmbed
					.setDescription("\n" +
							"\n" +
							"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque hendrerit faucibus lorem, in faucibus elit tempor in. Vestibulum fringilla mi erat, eget fringilla massa mollis sit amet. Fusce aliquam dolor laoreet, congue turpis a, iaculis eros. Donec ullamcorper ultricies justo quis bibendum. Aenean porttitor vehicula nisi quis venenatis. Donec id felis mauris. Mauris rhoncus a mi eget ultrices.\n" +
							"\nm eu nisi eu egestas. Nulla tincidunt enim ac eam. Curabitur suscipit magna id turpis porta. ").setColor(new Color(132, 197, 251))
					.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
			event.getChannel().sendMessage(messageEmbed.build()).queue();

		//			event.getGuild().getRoles();
//			  channel.sendMessage("Pong!").queue();


			}
	}

}
