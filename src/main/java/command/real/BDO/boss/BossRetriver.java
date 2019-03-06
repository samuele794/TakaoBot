package command.real.BDO.boss;

import beans.BDOBossBean.Giorno;
import com.google.gson.reflect.TypeToken;
import starter.Start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe per leggere il json dei boss
 */
public class BossRetriver {
	/**
	 * 
	 * @return ArrayList<Giorno>
	 */
	public static ArrayList<Giorno> getBossList() {

		String fileName = "jsonboss.json";
		ClassLoader classLoader = BossRetriver.class.getClassLoader();

		File file = new File(classLoader.getResource(fileName).getFile());

		ArrayList<Giorno> boss = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));

			boss = Start.gson.fromJson(reader, new TypeToken<ArrayList<Giorno>>() {
			}.getType());

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return boss;
	}

}
