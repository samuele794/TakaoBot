package interfaces;

import java.io.*;


/**
 * Classe per caricare il token di discord da file
 */
public class DiscordTokenInterfaces {

    public static String getToken() {

        if (new File("tokenDiscord.txt").exists()) {
            File file = new File("tokenDiscord.txt");

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String token = reader.readLine();
                reader.close();

                return token;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {

            File file= new File("tokenDiscord.txt");
            try {
                System.out.println("SuccessCreate: "+    file.createNewFile());
                FileWriter stream = new FileWriter(file);
                stream.write("<Sostituisci qui con il token>");
                stream.close();
                System.out.println("Inserire il token di discord nel file che trovi nella seguente cartella: \n" + file.getAbsolutePath());

            } catch (IOException e) {

            }
        }
        return null;
    }
}
