package testing;

import com.google.gson.Gson;
import interfaces.PostgreSQLInterface;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class StartTest {

	public static Gson gson = new Gson();
	public static JDA jda;

	public static void main(String[] args) throws IOException {

//		SQLiteInterfaces.initializeDB();
		PostgreSQLInterface.initializeDB();

		try {
			jda = new JDABuilder("NTMwMzA1MjEzNDkyNDk0MzM4.D3RW1A.IXqW6JxGvSrBHpstTXuZtVOyCO4").build();
		} catch (LoginException e) {
			e.printStackTrace();
			return;
		}

//		DiscordScheduler.startScheduling(jda);


		jda.addEventListener(new TestCommand());
//		jda.addEventListener(new PrivateMessage());

		System.out.println(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR));

	}

	public static JDA getJDA() {
		return jda;
	}


}
