package command.real.BDO.boss;

import command.pattern.ControlCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BossCommand extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		if (ControlCommand.controlCommand(event, "bossBDO")) {
			String authorID = event.getAuthor().getId();
			String ownerID = event.getGuild().getOwnerId();

			if (!ownerID.equals(authorID)) {
				event.getChannel().sendMessage(event.getAuthor().getName() + " non sei autorizzato all'uso di questo comando").queue();
			} else {
				EmbedBuilder builder = new EmbedBuilder().setImage("https://cdn.discordapp.com/attachments/520950024939634729/520951960636227584/wtb_eu.jpg");
				event.getChannel().sendMessage(builder.build()).queue();
			}
		}
	}
}
