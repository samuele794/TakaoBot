package beans.BDOBossBean;

import java.util.ArrayList;

public class Boss {
	private String[] nomeBoss;
	private String ora;

	public Boss(String[] nomeBoss, String ora) {
		setNomeBoss(nomeBoss);
		setOra(ora);
	}

	public static String[] getHourBoss(int ora, int minuto, ArrayList<Boss> lists) {
		String time = new StringBuilder(Integer.toString(0)).append(":").append(minuto).toString();
		for (int cont = 0; cont < lists.size(); cont++) {

			Boss boss = lists.get(cont);

			if (boss.getOra().equals(time)) {
				return boss.getNomeBoss();
			}

		}

		throw new IndexOutOfBoundsException("Orario non valido");
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
