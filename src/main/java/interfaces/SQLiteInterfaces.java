package interfaces;

import beans.ServerToChannel;
import org.apache.commons.text.StringEscapeUtils;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.ArrayList;

public class SQLiteInterfaces {

    private static Connection connection;
    private static final String SERVERS_DISCORD = "ServersDiscord";
    private static final String RSS_LINK = "RSSLink";

    /**
     * Nomi delle colonne sul db
     */
    public enum SD_DB_COLUMN {
        NAME_SERVER,
        SERVER_ID,
        SIMBOL_COMMAND,
        BDONewsIDChannel,
        BDOPatchIDChannel
    }

    public enum RSS_DB_COLUMN {
        ID,
        LastNewsBDO,
        LastPatchBDO
    }

    /**
     * Metodo per inizializzare la connessione e/o il DB + la tabella
     * <p>
     * PS: In caso sia il primo avvio del bot, accendere -> spegnere -> accendere il bot
     */
    public static void initializeDB() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            if (connection != null) {
                DatabaseMetaData metaData = connection.getMetaData();
            }

            String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + SERVERS_DISCORD + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                sql = "CREATE TABLE " + SERVERS_DISCORD +
                        "(" + SD_DB_COLUMN.SERVER_ID.name() + " VARCHAR(255) NOT NULL PRIMARY KEY, " +
                        SD_DB_COLUMN.NAME_SERVER.name() + " TEXT NOT NULL, " +
                        SD_DB_COLUMN.SIMBOL_COMMAND.name() + " VARCHAR(5) DEFAULT \"%\" NOT NULL," +
                        SD_DB_COLUMN.BDONewsIDChannel + " VARCHAR(255)," +
                        SD_DB_COLUMN.BDOPatchIDChannel + " VARCHAR(255))";
                statement.execute(sql);

                sql = "CREATE TABLE " + RSS_LINK + "TEXT default NULL, " +
                        "(" + RSS_DB_COLUMN.ID.name() + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        RSS_DB_COLUMN.LastNewsBDO.name() + "TEXT DEFAULT NULL, " +
                        RSS_DB_COLUMN.LastPatchBDO.name() + "TEXT DEFAULT NULL )";
                statement.execute(sql);

                sql = "INSERT INTO RSSLink DEFAULT VALUES";
                statement.execute(sql);

                statement.close();
                connection.endRequest();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo che aggiunge nome e l'id del server che ha aggiunto il bot sul database.
     *
     * @param nameServe Nome del server
     * @param serverID  ID del server Discord
     */
    public static void newServer(String nameServe, String serverID) {

        var sql = "INSERT INTO " + SERVERS_DISCORD + "(" +
                SD_DB_COLUMN.NAME_SERVER.name() + "," + SD_DB_COLUMN.SERVER_ID.name() + ")" +
                "VALUES(\"" + nameServe + "\", \"" + serverID + "\");";

        try {
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che rimuove la riga del server che ha espulso il bot
     *
     * @param serverID
     */
    public static void deleteServer(String serverID) {
        var sql = "DELETE FROM " + SERVERS_DISCORD + " WHERE " + SD_DB_COLUMN.SERVER_ID.name() + "='" + serverID + "';";

        try {
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo che per ottenre il simbolo del server
     *
     * @param serverID
     * @return
     */
    public static String getSimbol(String serverID) {
        var sql = "SELECT " + SD_DB_COLUMN.SIMBOL_COMMAND.name() + " FROM " + SERVERS_DISCORD + " WHERE " + SD_DB_COLUMN.SERVER_ID.name() + " = '" + serverID + "';";
        try {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return StringEscapeUtils.unescapeJava(resultSet.getString("SIMBOL_COMMAND"));
            } else {
                throw new RuntimeException("Errore");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Metodo per configurare il simbolo di comando per il server
     *
     * @param command  Simbolo del comando nuovo
     * @param serverID ID del server
     */
    public static void setSimbol(String command, String serverID) {
        var sql = "UPDATE " + SERVERS_DISCORD + " SET " + SD_DB_COLUMN.SIMBOL_COMMAND.name() + "= '" + command + "' " +
                "WHERE " + SD_DB_COLUMN.SERVER_ID.name() + "='" + serverID + "';";
        //TODO fare rinforzo contro injection
        sql = StringEscapeUtils.escapeJava(sql);
        try {
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per settare il canale per le news su BDO
     *
     * @param serverID
     * @param channelID
     */
    public static void setBDONewsChannel(String serverID, String channelID) {
        var sql = "UPDATE " + SERVERS_DISCORD + " SET " + SD_DB_COLUMN.BDONewsIDChannel.name() + "= '" + channelID + "' WHERE " + SD_DB_COLUMN.SERVER_ID.name() + "='" + serverID + "';";

        try {
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per ottenere tutti i canali registrati alle news di BDO
     *
     * @return
     */
    public static ArrayList<ServerToChannel> getBDONewsChannel() {
        var sql = "SELECT " + SD_DB_COLUMN.SERVER_ID.name() + ", " + SD_DB_COLUMN.BDONewsIDChannel.name() + " FROM " + SERVERS_DISCORD + " WHERE " + SD_DB_COLUMN.BDONewsIDChannel.name() + " IS NOT NULL";
        ArrayList<ServerToChannel> list = new ArrayList<>();

        try {
            var statement = connection.createStatement();
            var resutlt = statement.executeQuery(sql);

            while (resutlt.next()) {
                list.add(new ServerToChannel(resutlt.getString(SD_DB_COLUMN.SERVER_ID.name()), resutlt.getString(SD_DB_COLUMN.BDONewsIDChannel.name())));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }

    /**
     * Metodo per settare il canale per le patch di BDO
     *
     * @param serverID
     * @param channelID
     */
    public static void setBDOPatchChannel(String serverID, String channelID) {
        var sql = "UPDATE " + SERVERS_DISCORD + " SET " + SD_DB_COLUMN.BDOPatchIDChannel.name() + "= '" + channelID + "' WHERE " + SD_DB_COLUMN.SERVER_ID.name() + "='" + serverID + "';";

        try {
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per ottenere tutti i canali registrati alle patch di BDO
     *
     * @return
     */
    public static ArrayList<ServerToChannel> getBDOPatchChannel() {
        var sql = "SELECT " + SD_DB_COLUMN.SERVER_ID.name() + ", " + SD_DB_COLUMN.BDOPatchIDChannel.name() + " FROM " + SERVERS_DISCORD + " WHERE " + SD_DB_COLUMN.BDOPatchIDChannel.name() + " IS NOT NULL";
        ArrayList<ServerToChannel> list = new ArrayList<>();

        try {
            var statement = connection.createStatement();
            var resutlt = statement.executeQuery(sql);

            while (resutlt.next()) {
                list.add(new ServerToChannel(resutlt.getString(SD_DB_COLUMN.SERVER_ID.name()), resutlt.getString(SD_DB_COLUMN.BDOPatchIDChannel.name())));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }

    /**
     * Imposta l'ultima notizia di BDO, se esiste l'aggiorna, altrimenti inserisce la riga.
     *
     * @param url
     */
    public static void setLastNewsBDO(String url) {
        var linkLastNews = getLastNewsBDO();

        @Language("SQLite") var sql = "UPDATE " + RSS_LINK + " SET " + RSS_DB_COLUMN.LastNewsBDO.name() + " = \"" + url + "\" WHERE " + RSS_DB_COLUMN.ID.name() + "  = 1;";
        try {
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Ottiene il link dell'ultima notizia di BDO
     *
     * @return
     */
    public static String getLastNewsBDO() {
        var sql = "SELECT " + RSS_DB_COLUMN.LastNewsBDO.name() + " FROM " + RSS_LINK + ";";

        try {
            var statement = connection.createStatement();
            var result = statement.executeQuery(sql);

            //false è vuoto
            //true trovato
            if (result.next()) {
                return result.getString(RSS_DB_COLUMN.LastNewsBDO.name());
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ottiene il link dell'ultima patch di BDO
     *
     * @return String il link dell'ultima patch
     */
    public static String getLastPatchBDO() {
        var sql = "SELECT " + RSS_DB_COLUMN.LastPatchBDO.name() + " FROM " + RSS_LINK + ";";

        try {
            var statement = connection.createStatement();
            var result = statement.executeQuery(sql);

            //false è vuoto
            //true trovato
            if (result.next()) {
                return result.getString(RSS_DB_COLUMN.LastPatchBDO.name());
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Imposta l'ultima patch di BDO, se esiste l'aggiorna, altrimenti inserisce la riga.
     *
     * @param url
     */
    public static void setLastPatchBDO(String url) {
        var linkLastNews = getLastPatchBDO();

            var sql = "UPDATE " + RSS_LINK + " SET " + RSS_DB_COLUMN.LastPatchBDO.name() + " = '" + url + "' WHERE " + RSS_DB_COLUMN.ID.name() + " = 1;";
            try {
                var statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }


}
