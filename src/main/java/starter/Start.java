package starter;

import com.google.gson.Gson;
import command.real.BDO.RSS.BDONewsStartCommand;
import command.real.BDO.RSS.BDONewsStopCommand;
import command.real.BDO.RSS.BDOPatchStartCommand;
import command.real.BDO.RSS.BDOPatchStopCommand;
import command.real.BDO.boss.BDOBossStartCommand;
import command.real.BDO.boss.BDOBossStopCommand;
import command.real.JoinListener;
import command.real.configuration.ConfigurationCommand;
import command.real.configuration.HelpCommand;
import interfaces.DiscordScheduler;
import interfaces.DiscordTokenInterfaces;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Start {

	public static Gson gson = new Gson();
	public static JDA jda = null;

	public static void main(String[] args) {

		SQLiteInterfaces.initializeDB();

		try {
			jda = new JDABuilder(DiscordTokenInterfaces.getToken()).build();
		} catch (LoginException e) {
			e.printStackTrace();
			return;
		}

		DiscordScheduler.startScheduling(jda);

		//command testing
		// jda.addEventListener(new BossCommand());

		jda.addEventListener(new JoinListener());
		jda.addEventListener(new ListenerAdapter() {
			@Override
			public void onMessageReceived(MessageReceivedEvent event) {
				if (!event.getAuthor().isBot()) {
					System.out.println("Comando inviato da server: " + event.getGuild().getName());
					System.out.println("Inviato da: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator());
				}
			}
		});

		//comandi reali
		jda.addEventListener(new HelpCommand());
		jda.addEventListener(new ConfigurationCommand());
		jda.addEventListener(new BDONewsStartCommand());
		jda.addEventListener(new BDONewsStopCommand());
		jda.addEventListener(new BDOPatchStartCommand());
		jda.addEventListener(new BDOPatchStopCommand());
		jda.addEventListener(new BDOBossStartCommand());
		jda.addEventListener(new BDOBossStopCommand());

		System.out.println(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR));

	}
}
