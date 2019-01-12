import com.google.gson.Gson;
import interfaces.DiscordTokenInterfaces;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import testing.PrivateMessage;
import testing.TestCommand;

import javax.security.auth.login.LoginException;

public class Start {

    public static Gson gson = new Gson();
    public static JDA jda = null;

    public static void main(String args[]) {

        SQLiteInterfaces.initializeDB();

        try {
            jda = new JDABuilder(DiscordTokenInterfaces.getToken()).build();
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }


        jda.addEventListener(new TestCommand());
        jda.addEventListener(new PrivateMessage());

        System.out.println(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR));

    }
}
