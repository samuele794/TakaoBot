package command.real;

import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import starter.Start;

public class JoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        SQLiteInterfaces.newServer(event.getGuild().getName(), event.getGuild().getId());
//        event.getGuild().getController().createRole().setName("BossAlert").setMentionable(true).queue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        SQLiteInterfaces.deleteServer(event.getGuild().getId());
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        SQLiteInterfaces.deleteServer(event.getGuild().getId());

    }
}
