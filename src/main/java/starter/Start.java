package starter;

import com.google.gson.Gson;
import command.real.BDO.BOSS.BossCommand;
import command.real.BDO.RSS.BDONewsStartCommand;
import command.real.BDO.RSS.BDOPatchStartCommand;
import command.real.JoinListener;
import command.real.configuration.ConfigurationCommand;
import command.testing.PrivateMessage;
import command.testing.TestCommand;
import interfaces.DiscordTokenInterfaces;
import interfaces.RSS.Scheduler;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;

import javax.security.auth.login.LoginException;

public class Start {

    public static Gson gson = new Gson();

    public static void main(String args[]) {

        SQLiteInterfaces.initializeDB();

        JDA jda = null;
        try {
            jda = new JDABuilder(DiscordTokenInterfaces.getToken()).build();
        } catch (LoginException e) {
            e.printStackTrace();
            return;


        }

        Scheduler.startScheduling(jda);


        //command testing
        jda.addEventListener(new TestCommand());
        jda.addEventListener(new PrivateMessage());
        jda.addEventListener(new BossCommand());

        //comandi reali
        jda.addEventListener(new JoinListener());
        jda.addEventListener(new ConfigurationCommand());
        jda.addEventListener(new BDONewsStartCommand());
        jda.addEventListener(new BDOPatchStartCommand());

        System.out.println(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR));

    }
}
