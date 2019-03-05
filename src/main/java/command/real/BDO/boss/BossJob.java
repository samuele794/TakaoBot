package command.real.BDO.boss;

import beans.BDOBossBean.Boss;
import beans.BDOBossBean.Giorno;
import beans.ServerToChannel;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.MessageBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import static starter.Start.jda;

public class BossJob implements Job {

	public static void processHour(LocalDateTime time, Giorno giorno) {

		switch (time.getHour()) {
			case 0: {
				//00:00-00:15
				processMinute0015(time, giorno);
			}
			break;
			case 1: {
				//1:45-1:55
				processMinute4500(time, giorno);
			}
			break;
			case 2: {
				//2:00
				processMinute4500(time, giorno);
			}
			break;
			case 4: {
				//4:45-4:55
				processMinute4500(time, giorno);
			}
			break;
			case 5: {
				//5:00
				processMinute4500(time, giorno);
			}
			break;
			case 8: {
				//8:45-8:55
				processMinute4500(time, giorno);
			}
			break;
			case 9: {
				//9:00
				processMinute4500(time, giorno);
			}
			break;
			case 11: {
				//11:45-11:55
				processMinute4500(time, giorno);
			}
			break;
			case 12: {
				//12:00
				processMinute4500(time, giorno);
			}
			break;
			case 15: {
				//15:45.15:55
				processMinute4500(time, giorno);
			}
			break;
			case 16: {
				//16:00
				processMinute4500(time, giorno);
			}
			break;
			case 18: {
				//18:45-18:55
				processMinute4500(time, giorno);
			}
			break;
			case 19: {
				//19:00
				processMinute4500(time, giorno);
			}
			break;
			case 22: {
				//22:00-22:15
				processMinute0015(time, giorno);
			}
		}
	}

	private static void processMinute4500(LocalDateTime time, Giorno giorno) {
		switch (time.getMinute()) {
			case 45: {
				Boss.getHourBoss(time.getHour() + 1, 0, giorno.getBosses());
			}
			break;
			case 50: {
				Boss.getHourBoss(time.getHour() + 1, 0, giorno.getBosses());
			}
			break;
			case 55: {
				Boss.getHourBoss(time.getHour() + 1, 0, giorno.getBosses());
			}
		}

		if (time.getMinute() == 0) {
			Boss.getHourBoss(time.getHour(), 0, giorno.getBosses());
		}
	}

	private static void processMinute0015(LocalDateTime time, Giorno giorno) {
		switch (time.getMinute()) {
			case 0: {
				String[] listBoss = Boss.getHourBoss(time.getHour(), 15, giorno.getBosses());
				publish(listBoss, "15", time.format(DateTimeFormatter.ofPattern("HH:mm")));

			}
			break;
			case 5: {
				String[] listBoss = Boss.getHourBoss(time.getHour(), 15, giorno.getBosses());
				publish(listBoss, "10", time.format(DateTimeFormatter.ofPattern("HH:mm")));

			}
			break;
			case 10: {
				String[] listBoss = Boss.getHourBoss(time.getHour(), 15, giorno.getBosses());
				publish(listBoss, "5", time.format(DateTimeFormatter.ofPattern("HH:mm")));

			}

		}

		if (time.getMinute() == 15) {
			String[] listBoss = Boss.getHourBoss(time.getHour(), 15, giorno.getBosses());
			publish(listBoss, "0", time.format(DateTimeFormatter.ofPattern("HH:mm")));

		}
	}

	private static void publish(String[] bosses, String orarioMancante, String oraAttuale) {
		ArrayList<ServerToChannel> listServerChannel = SQLiteInterfaces.getBDOBossChannel();

		MessageBuilder builder = new MessageBuilder();
		builder.append(oraAttuale).append(" ");

		for (String boss : bosses) {
			builder.append(boss).append(" ");
		}

		if (orarioMancante.equals("0")) {
			builder.append("sta spawnando");
		} else {
			builder.append("in arrivo tra: ").append(orarioMancante);
		}

		for (ServerToChannel channel : listServerChannel) {
			jda.getGuildById(channel.getServerID()).getTextChannelById(channel.getChannelID()).sendMessage(builder.build()).queue();
		}

	}

	@Override
	public void execute(JobExecutionContext context) {
		LocalDateTime time = LocalDateTime.now();
		ArrayList<Giorno> list = BossRetriver.getBossList();
		Giorno booDay = Giorno.getDayBosses(time.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH), list);
		processHour(time, booDay);

	}

}