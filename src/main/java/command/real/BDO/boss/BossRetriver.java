package command.real.BDO.boss;

import beans.BDOBossBean.Boss;
import beans.BDOBossBean.Giorno;
import beans.ServerToChannel;
import com.google.gson.reflect.TypeToken;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import starter.Start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static starter.Start.jda;

/**
 * Classe per leggere il json dei boss
 */
public class BossRetriver {
	/**
	 * @return ArrayList<Giorno>
	 */
	public static ArrayList<Giorno> getBossList() {

		String fileName = "jsonboss.json";
		ClassLoader classLoader = BossRetriver.class.getClassLoader();

		File file = new File("resources/" + fileName);

		if (!file.exists()) {
			//risorsa su IDE
			file = new File(classLoader.getResource(fileName).getFile());
		}

		ArrayList<Giorno> boss = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));

			boss = Start.gson.fromJson(reader, new TypeToken<ArrayList<Giorno>>() {
			}.getType());

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return boss;
	}

	public static void processHour(int ora, int minuto, Giorno giorno) {
		System.out.println("Giorno " + giorno.getGiorno());
		System.out.println("Ora " + ora);
		if (ora == 0) {
			//00:00-00:15
			processMinute0015(ora, minuto, giorno);
		} else if (ora == 1) {
			//1:45-1:55
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 2) {
			//2:00
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 4) {
			//4:45-4:55
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 5) {
			//5:00
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 8) {
			//8:45-8:55
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 9) {
			//9:00
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 11) {
			//11:45-11:55
			System.out.println("11");
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 12) {
			//12:00
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 15) {
			//15:45.15:55
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 16) {
			//16:00
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 18) {
			//18:45-18:55
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 19) {
			//19:00
			processMinute4500(ora, minuto, giorno);
		} else if (ora == 22) {
			//22:00-22:15
			processMinute0015(ora, minuto, giorno);
		}

	}

	private static void processMinute4500(int ora, int minuto, Giorno giorno) {
		System.out.println("Minuto " + minuto);
		if (minuto == 45) {
			String[] listBoss = Boss.getHourBoss(ora + 1, 0, giorno.getBosses());
			publish(listBoss, "15");
		} else if (minuto == 50) {
			System.out.println("Minuti 50");
			String[] listBoss = Boss.getHourBoss(ora + 1, 0, giorno.getBosses());
			publish(listBoss, "10");
		} else if (minuto == 55) {
			System.out.println("Minuti 55");
			String[] listBoss = Boss.getHourBoss(ora + 1, 0, giorno.getBosses());
			publish(listBoss, "5");
		} else if (minuto == 0) {
			System.out.println("Minuti 00");
			String[] listBoss = Boss.getHourBoss(ora, 0, giorno.getBosses());
			publish(listBoss, "0");
		}
	}

	private static void processMinute0015(int ora, int minuto, Giorno giorno) {
		if (minuto == 0) {
			String[] listBoss = Boss.getHourBoss(ora, 15, giorno.getBosses());
			publish(listBoss, "15");

		} else if (minuto == 5) {
			String[] listBoss = Boss.getHourBoss(ora, 15, giorno.getBosses());
			publish(listBoss, "10");

		} else if (minuto == 10) {
			String[] listBoss = Boss.getHourBoss(ora, 15, giorno.getBosses());
			publish(listBoss, "5");

		} else if (minuto == 15) {
			System.out.println("caso 15");
			String[] listBoss = Boss.getHourBoss(ora, 15, giorno.getBosses());
			publish(listBoss, "0");
		}
	}

	private static void publish(String[] bosses, String orarioMancante) {
		ArrayList<ServerToChannel> listServerChannel = SQLiteInterfaces.getBDOBossChannel();

		String oraAttuale = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));


		System.out.println("Boss " + Arrays.toString(bosses));
		System.out.println("Ora attuale" + oraAttuale);

		MessageBuilder builder = new MessageBuilder();
		builder.append(oraAttuale).append(" -> 	");

		for (String boss : bosses) {
			builder.append(boss).append(" ");
		}

		if (orarioMancante.equals("0")) {
			builder.append("sta spawnando");
		} else {
			builder.append("in arrivo tra: ").append(orarioMancante).append(" minuti");
		}
		Iterator<ServerToChannel> iterator = listServerChannel.iterator();
		while (iterator.hasNext()) {
			ServerToChannel channel = iterator.next();
			jda.getGuildById(channel.getServerID()).getTextChannelById(channel.getChannelID()).sendMessage(builder.build()).queue();
		}

	}

}
