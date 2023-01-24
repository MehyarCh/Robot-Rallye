package Desperatedrosseln.Local.Protocols;

public class ConnectionUpdate {

    private int clientID;

    private boolean isConnected;

    public int getClientID() {
        return clientID;
    }

    public boolean getIsConnected() {
        return isConnected; //false
    }

    public ConnectionUpdate(){
        //do something (kick, reconnect, etc.)
    }
}
