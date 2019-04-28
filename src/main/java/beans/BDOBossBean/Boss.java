package beans.BDOBossBean;

import java.util.ArrayList;

/**
 * Rappresentazione dei boss
 */
public class Boss {
	/**
	 * Lista boss dell'ora
	 */
	private String[] nomeBoss;

	/**
	 * Ora di spawn
	 */
	private String ora;

	public Boss(String[] nomeBoss, String ora) {
		setNomeBoss(nomeBoss);
		setOra(ora);
	}

	/**
	 * Metodo per ottenere i boss dell'ora
	 *
	 * @param ora    Ora di spawn del boss
	 * @param minuto Minuto di spawn del boss
	 * @param lists  Lista dei boss del giorno
	 * @return Lista dei boss dell'ora indicata
	 */
	public static String[] getHourBoss(int ora, int minuto, ArrayList<Boss> lists) {
		String time = new StringBuilder(Integer.toString(ora)).append(":").append(minuto).toString();
		for (int cont = 0; cont < lists.size(); cont++) {

			Boss boss = lists.get(cont);

			if (boss.getOra().equals(time)) {
				return boss.getNomeBoss();
			}

		}

		return null;
	}

	/**
	 * Ottieni i nomi dei boss
	 * @return
	 */
	public String[] getNomeBoss() {
		return nomeBoss;
	}

	private void setNomeBoss(String[] nomeBoss) {
		this.nomeBoss = nomeBoss;
	}


	private String getOra() {
		return ora;
	}

	private void setOra(String ora) {
		this.ora = ora;
	}
}
