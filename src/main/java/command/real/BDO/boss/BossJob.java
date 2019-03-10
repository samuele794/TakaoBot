package command.real.BDO.boss;

import beans.BDOBossBean.Giorno;
import interfaces.TakaoLog;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Job per l'ottenimento dei boss
 */
public class BossJob implements Job {


	@Override
	public void execute(JobExecutionContext context) {
		LocalDateTime time = LocalDateTime.now();
		TakaoLog.logInfo(time.getHour() + ":" + time.getMinute());
		ArrayList<Giorno> list = BossRetriver.getBossList();
		Giorno booDay = Giorno.getDayBosses(time.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH), list);
		BossRetriver.processHour(time.getHour(), time.getMinute(), booDay);

	}

}