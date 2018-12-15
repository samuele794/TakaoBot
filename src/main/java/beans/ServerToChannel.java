package beans;

/**
 * Classe di appoggio per ottenere serverID e channelID
 * per i publish del feed RSS
 */
public class ServerToChannel {

    private String serverID;
    private String channelID;

    public ServerToChannel (String serverID, String channelID){
        setServerID(serverID);
        setChannelID(channelID);
    }

    /**
     * Metodo per ottenere il serverID
     *
     * @return serverID
     */
    public String getServerID() {
        return serverID;
    }

    /**
     * Metodo per impostare il serverID
     * @param serverID id del server discord
     */

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    /**
     * Metodo per ottenere il channelID
     * @return String id del canale
     */
    public String getChannelID() {
        return channelID;
    }

    /**
     * Metodo per impostare il channelID
     * @param channelID id del canale testuale
     */
    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }
}
