package testing.bossBDO;

import java.util.ArrayList;

public class Giorno {

    private Giorni giorno;
    private ArrayList<Boss> bosses;

    public enum Giorni {
        LUNEDI,
        MARTEDI,
        MERCOLEDI,
        GIOVEDI,
        VENERDI,
        SABATO,
        DOMENICA
    }

    public Giorno(Giorni giorno, ArrayList<Boss> bosses) {
        setBosses(bosses);
        setGiorno(giorno);
    }

    public Giorni getGiorno() {
        return giorno;
    }

    public void setGiorno(Giorni giorno) {
        this.giorno = giorno;
    }

    public ArrayList<Boss> getBosses() {
        return bosses;
    }

    public void setBosses(ArrayList<Boss> bosses) {
        this.bosses = bosses;
    }
}
