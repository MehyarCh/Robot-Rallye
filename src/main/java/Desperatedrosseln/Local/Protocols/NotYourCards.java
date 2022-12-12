package Desperatedrosseln.Local.Protocols;

public class NotYourCards {

    /*
    Body:
        "clientID": 42,
        "cardsInHand": 9
     */
    private int clientID;
    private int cardsInHand;

    public int getClientID() {
        return clientID;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

    public NotYourCards(int clientID, int cardsInHand){
        this.clientID = clientID;
        this.cardsInHand = cardsInHand;
    }
}
