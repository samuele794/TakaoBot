package beans;

public class ServerToChannel {

    private String serverID;
    private String channelID;

    public ServerToChannel (String serverID, String channelID){
        setServerID(serverID);
        setChannelID(channelID);
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }
}
