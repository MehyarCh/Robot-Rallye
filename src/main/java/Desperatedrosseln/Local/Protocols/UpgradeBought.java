package Desperatedrosseln.Local.Protocols;

public class UpgradeBought {
    int clientID;
    String card;

    public UpgradeBought(int clientID, String card) {
        this.clientID = clientID;
        this.card = card;
    }

    public int getClientID() {
        return clientID;
    }

    public String getCard() {
        return card;
    }
}
