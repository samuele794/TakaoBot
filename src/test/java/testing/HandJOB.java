package testing;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

import static starter.Start.jda;

public class HandJOB implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Cron");
        jda.getGuildById("324517087416549377").getTextChannelById("514119199677743104").sendMessage("TTTTT").queue();

        var t = LocalDateTime.now();
        var minute = t.getMinute();

        if (minute == 0) {

        }
        if (minute == 5) {

        }
        if (minute == 10) {

        }

        if (minute == 15) {

        }

    }
}