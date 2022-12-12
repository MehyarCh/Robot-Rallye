package Desperatedrosseln.Local.Protocols;

public class Energy {

    /*
    Body:
        "clientID": 42,
        "count": 1,
        "source": "EnergySpace"
     */
    private int clientID;
    private int count;
    private String source;

    public int getClientID() {
        return clientID;
    }

    public int getCount() {
        return count;
    }

    public String getSource() {
        return source;
    }

    public Energy (int clientID, int count, String source){
        this.clientID = clientID;
        this.count = count;
        this.source = source;
    }
}
