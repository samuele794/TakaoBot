package testing;

import com.google.gson.Gson;
import interfaces.SQLiteInterfaces;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import testing.bossBDO.Boss;
import testing.bossBDO.Giorno;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public class StartTest {

    public static Gson gson = new Gson();
    public static JDA jda;

    public static void main(String args[]) {

        Boss boss1 = new Boss(new String[]{"Kutum", "Kzarka"}, "00:15");
        Boss boss2 = new Boss(new String[]{"Kzarka"}, "02:00");

        ArrayList<Boss> arrayList = new ArrayList<>();
        arrayList.add(boss1);
        arrayList.add(boss2);

        Giorno lunedi = new Giorno(Giorno.Giorni.LUNEDI, arrayList);
        Giorno martedi = new Giorno(Giorno.Giorni.MARTEDI, arrayList);

        ArrayList<Giorno> lg = new ArrayList<>();
        lg.add(lunedi);
        lg.add(martedi);

        Gson gson = new Gson();
        gson.toJson(lg.toArray());

        SQLiteInterfaces.initializeDB();

        try {
            jda = new JDABuilder("").build();
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }


        jda.addEventListener(new TestCommand());
        jda.addEventListener(new PrivateMessage());

        System.out.println(jda.asBot().getInviteUrl(Permission.ADMINISTRATOR));

    }

    public static JDA getJDA() {
        return jda;
    }
}
