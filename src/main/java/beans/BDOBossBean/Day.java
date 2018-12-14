package beans.BDOBossBean;

import java.util.ArrayList;

public class Day {
    private String giorno;
    private Boss listaBoss;

    public Day(String giorno, Boss listaBoss){
        setGiorno(giorno);
        setListaBoss(listaBoss);
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public Boss getListaBoss() {
        return listaBoss;
    }

    public void setListaBoss(Boss listaBoss) {
        this.listaBoss = listaBoss;
    }
}
