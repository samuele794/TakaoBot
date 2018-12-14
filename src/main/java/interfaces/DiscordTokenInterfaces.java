package interfaces;

import java.io.*;

public class DiscordTokenInterfaces {
    public static String getToken(){
    File file = new File(ClassLoader.getSystemClassLoader().getResource("tokenDiscord.txt").getPath());

        if (file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String token = reader.readLine();
                reader.close();

                return token;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            throw new RuntimeException("Token non esistente");
        }
        return null;
    }
}
