package starter;

import com.google.gson.Gson;
import command.real.BDO.RSS.BDONewsStartCommand;
import command.real.BDO.RSS.BDONewsStopCommand;
import command.real.BDO.RSS.BDOPatchStartCommand;
import command.real.BDO.RSS.BDOPatchStopCommand;
import command.real.JoinListener;
import command.real.configuration.ConfigurationCommand;
import command.real.configuration.HelpCommand;
import interfaces.DiscordTokenInterfaces;
import interfaces.RSS.RSScheduler;
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

        RSScheduler.startScheduling(jda);

        //command testing
        // jda.addEventListener(new BossCommand());

        jda.addEventListener(new JoinListener());
        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(MessageReceivedEvent event) {
                System.out.println("Comando inviato da server: " + event.getGuild().getName());
                System.out.println("Inviato da: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator());
            }
        });

        //comandi reali
        jda.addEventListener(new HelpCommand());
        jda.addEventListener(new ConfigurationCommand());
        jda.addEventListener(new BDONewsStartCommand());
        jda.addEventListener(new BDONewsStopCommand());
        jda.addEventListener(new BDOPatchStartCommand());
        jda.addEventListener(new BDOPatchStopCommand());

        System.out.println(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR));

    }
}
