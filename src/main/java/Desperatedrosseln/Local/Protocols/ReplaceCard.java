package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

public class ReplaceCard {

    /*
    Body:
        "register": 3,
        "newCard": "MoveI",
        "clientID": 9001
     */

    private int register;
    private Card newCard;
    private int clientID;

    public int getRegister() {
        return register;
    }

    public Card getNewCard() {
        return newCard;
    }

    public int getClientID() {
        return clientID;
    }

    public ReplaceCard(int register, Card newCard, int clientID) {
        this.register = register;
        this.newCard = newCard;
        this.clientID = clientID;
    }
}
