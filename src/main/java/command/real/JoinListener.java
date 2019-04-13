package command.real;

import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
		PostgreSQLInterface.newServer(event.getGuild().getName(), event.getGuild().getId());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
		PostgreSQLInterface.deleteServer(event.getGuild().getId());
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
		PostgreSQLInterface.deleteServer(event.getGuild().getId());

	}

	@Override
	public void onGuildUpdateName(GuildUpdateNameEvent event) {
		event.getNewName();
	}

}
