package beans.BDOBossBean;

import java.util.ArrayList;

public class Giorno {

	private String giorno;
	private ArrayList<Boss> bosses;

	public Giorno(String giorno, ArrayList<Boss> bosses) {
		setBosses(bosses);
		setGiorno(giorno);
	}

	public static Giorno getDayBosses(String giorno, ArrayList<Giorno> list) {
		giorno = giorno.trim().toUpperCase();
		if(list != null & (giorno != null | giorno.equals("")) ){
			for (int cont = 0; cont < list.size(); cont++) {
				Giorno bossListDay = list.get(cont);
				if (bossListDay.getGiorno().equals(giorno)) {
					return bossListDay;
				}
			}

			throw new IndexOutOfBoundsException("Giorno non trovato");
		}else {
			throw new NullPointerException("Qualche parametro a null");
		}

	}

	public String getGiorno() {
		return giorno;
	}

	private void setGiorno(String giorno) {
		this.giorno = giorno;
	}

	public ArrayList<Boss> getBosses() {
		return bosses;
	}

	private void setBosses(ArrayList<Boss> bosses) {
		this.bosses = bosses;
	}
}
