package beans.BDOBossBean;

import java.util.ArrayList;

public class Boss {

    private String orario;
    private ArrayList<String> boss;

    public Boss (String orario, ArrayList<String> boss){
        setBoss(boss);
        setOrario(orario);
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }

    public ArrayList<String> getBoss() {
        return boss;
    }

    public void setBoss(ArrayList<String> boss) {
        this.boss = boss;
    }
}
