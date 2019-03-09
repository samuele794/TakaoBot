package command.real.BDO.boss;

import beans.BDOBossBean.Giorno;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class BossJob implements Job {


	@Override
	public void execute(JobExecutionContext context) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(time.getHour() + ":" + time.getMinute());
		ArrayList<Giorno> list = BossRetriver.getBossList();
		Giorno booDay = Giorno.getDayBosses(time.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH), list);
		BossRetriver.processHour(time.getHour(), time.getMinute(), booDay);

	}

}