package testing.bossBDO;

public class Boss {
    private String[] nomeBoss;
    private String ora;

    public Boss(String[] nomeBoss, String ora) {
        setNomeBoss(nomeBoss);
        setOra(ora);
    }

    public String[] getNomeBoss() {
        return nomeBoss;
    }

    public void setNomeBoss(String[] nomeBoss) {
        this.nomeBoss = nomeBoss;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }
}
