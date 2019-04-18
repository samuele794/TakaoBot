package starter;

import com.google.gson.Gson;
import command.real.BDO.BDOReceiver;
import command.real.JoinListener;
import command.real.configuration.ConfigurationCommand;
import command.real.configuration.HelpCommand;
import command.real.configuration.InfoCommand;
import command.real.sound.PlayerControlCommand;
import command.real.tpl.atmAlert.ATMAlertReceiver;
import interfaces.DiscordScheduler;
import interfaces.DiscordTokenInterfaces;
import interfaces.PostgreSQLInterface;
import interfaces.TakaoLog;
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

		PostgreSQLInterface.initializeDB();

		try {
			jda = new JDABuilder(DiscordTokenInterfaces.getToken()).build();
		} catch (LoginException e) {
			TakaoLog.logError(e.getMessage());
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
		jda.addEventListener(new InfoCommand());
		jda.addEventListener(new ConfigurationCommand());
		jda.addEventListener(new BDOReceiver());
		jda.addEventListener(new PlayerControlCommand());
		jda.addEventListener(new ATMAlertReceiver());

		System.out.println(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR));

	}
}
