package interfaces;

import command.real.BDO.RSS.BDORSScheduler;
import command.real.BDO.boss.BossJob;
import net.dv8tion.jda.core.JDA;
import org.apache.logging.log4j.LogManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Timer;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class DiscordScheduler {

	public static void startScheduling(JDA jda) {
		Timer timerTask = new Timer();
		timerTask.scheduleAtFixedRate(BDORSScheduler.taskFeedRSSBDO(jda), 1800000, 1800000);

		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler scheduler = sf.getScheduler();
			JobDetail job = newJob(BossJob
					.class)
					.withIdentity("bossJob", "group1")
					.build();

			CronTrigger trigger = newTrigger()
					.withIdentity("BossCronTrigger", "group1")
					.withSchedule(cronSchedule("0 0/5 * 1/1 * ? *"))
					.build();

			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException ex) {
			LogManager.getLogger().error("Errore nel job boss", ex);
		}

	}
}
