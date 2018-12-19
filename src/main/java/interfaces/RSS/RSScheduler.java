package interfaces.RSS;

import beans.ServerToChannel;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RSScheduler {

    private static Timer timerTask;

    public static void startScheduling(JDA jda) {
        timerTask = new Timer();
        timerTask.scheduleAtFixedRate(taskFeedRSSBDO(jda), 1800000, 1800000);
    }

    public static TimerTask taskFeedRSSBDO(JDA jda) {
        var task = new TimerTask() {
            @Override
            public void run() {


                var rssNewsMessage = RSSReader.readRSS("https://community.blackdesertonline.com/index.php?forums/news-announcements.181/index.rss");
                var rssPatchMessage = RSSReader.readRSS("https://community.blackdesertonline.com/index.php?forums/patch-notes.5/index.rss");

                //publishRSSNews
                if (SQLiteInterfaces.getLastNewsBDO() != null) {
                    if (!SQLiteInterfaces.getLastNewsBDO().equals(rssNewsMessage.getLink())) {
                        var newsMessage = RSSReader.prepareRSStoEmbeddedMessage(rssNewsMessage);
                        ArrayList<ServerToChannel> listNews = SQLiteInterfaces.getBDONewsChannel();
                        publishMessage(newsMessage, listNews, jda);
                        SQLiteInterfaces.setLastNewsBDO(rssNewsMessage.getLink());
                    }
                } else {
                    var newsMessage = RSSReader.prepareRSStoEmbeddedMessage(rssNewsMessage);
                    ArrayList<ServerToChannel> listNews = SQLiteInterfaces.getBDONewsChannel();
                    publishMessage(newsMessage, listNews, jda);
                    SQLiteInterfaces.setLastNewsBDO(rssNewsMessage.getLink());
                }

                //publishRSSPatch
                if (SQLiteInterfaces.getLastPatchBDO() != null) {
                    if (!SQLiteInterfaces.getLastPatchBDO().equals(rssPatchMessage.getLink())) {
                        var patchMessage = RSSReader.prepareRSStoEmbeddedMessage(rssPatchMessage);
                        ArrayList<ServerToChannel> patchhNews = SQLiteInterfaces.getBDOPatchChannel();
                        publishMessage(patchMessage, patchhNews, jda);
                        SQLiteInterfaces.setLastPatchBDO(rssPatchMessage.getLink());
                    }
                } else {
                    var patchMessage = RSSReader.prepareRSStoEmbeddedMessage(rssPatchMessage);
                    ArrayList<ServerToChannel> patchhNews = SQLiteInterfaces.getBDOPatchChannel();
                    publishMessage(patchMessage, patchhNews, jda);
                    SQLiteInterfaces.setLastPatchBDO(rssPatchMessage.getLink());
                }
            }
        };

        return task;
    }

    private static void publishMessage(MessageEmbed newsMessage, ArrayList<ServerToChannel> patchhNews, JDA jda) {
        for (var obj : patchhNews) {
            var channelID = obj.getChannelID();
            var serverID = obj.getServerID();
            jda.getGuildById(serverID).getTextChannelById(channelID).sendMessage(newsMessage).queue();
        }
    }

    public static void TaskBoss(JDA jda) {

        //ottenimento boss
        File file = new File(ClassLoader.getSystemClassLoader().getResource("jsonboss.json").getPath());
        var builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = builder.toString();


        var calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.get(Calendar.MINUTE);


    }
}
