package testing;

import command.pattern.ControlCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class TestCommand extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		// We don't want to respond to other bot accounts, including ourself
		Message message = event.getMessage();
		String content = message.getContentRaw();
		// getContentRaw() is an atomic getter
		// getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
        /*if (content.equals("!ping")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
        }*/

		if (ControlCommand.controlCommand(event, "ping")) {
			MessageChannel channel = event.getChannel();
//            channel.sendMessage("Pong!").queue();


//             event.getJDA().getTextChannelById("533348957649109002").getHistoryBefore("541393314721693696", 50).queue(messageHistory -> {
//                 messageHistory.getChannel();
//             });

           /* SchedulerFactory sf = new StdSchedulerFactory();
            try {
                Scheduler scheduler= sf.getScheduler();
                JobDetail job = newJob(HandJOB.class)
                        .withIdentity("job1", "group1")
                        .build();

                CronTrigger trigger = newTrigger()
                        .withIdentity("trigger1", "group1")
                        .withSchedule(cronSchedule("0 0/5 0 * * ? *"))
                        .build();

                scheduler.scheduleJob(job, trigger);
                scheduler.deleteJob(new JobKey("job1", "group1"));
                scheduler.start();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }*/
		}
	}

}
