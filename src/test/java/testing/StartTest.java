package testing;

import com.google.gson.Gson;
import command.real.sound.PlayerControlCommand;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class StartTest {

	public static Gson gson = new Gson();
	public static JDA jda;

	public static void main(String args[]) throws GeneralSecurityException, IOException {

//        Boss boss1 = new Boss(new String[]{"Kutum", "Kzarka"}, "00:15");
//        Boss boss2 = new Boss(new String[]{"Kzarka"}, "02:00");
//
//        ArrayList<Boss> arrayList = new ArrayList<>();
//        arrayList.add(boss1);
//        arrayList.add(boss2);
//
//        Giorno lunedi = new Giorno(Giorno.Giorni.LUNEDI, arrayList);
//        Giorno martedi = new Giorno(Giorno.Giorni.MARTEDI, arrayList);
//
//        ArrayList<Giorno> lg = new ArrayList<>();
//        lg.add(lunedi);
//        lg.add(martedi);
//
//        Gson gson = new Gson();
//        gson.toJson(lg.toArray());

		SQLiteInterfaces.initializeDB();


		try {
			jda = new JDABuilder("NTMwMzA1MjEzNDkyNDk0MzM4.DyPQMw.JJdFy0eybfmA9tuab4vU_Te__BI").build();
		} catch (LoginException e) {
			e.printStackTrace();
			return;
		}

//		DiscordScheduler.startScheduling(jda);


		jda.addEventListener(new TestCommand());
//		jda.addEventListener(new PrivateMessage());
		jda.addEventListener(new PlayerControlCommand());

		System.out.println(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR));

	}

	public static JDA getJDA() {
		return jda;
	}


}
