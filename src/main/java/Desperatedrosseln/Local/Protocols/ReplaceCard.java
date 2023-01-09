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
    private String newCard;
    private int clientID;

    public int getRegister() {
        return register;
    }

    public String getNewCard() {
        return newCard;
    }

    public int getClientID() {
        return clientID;
    }

    public ReplaceCard(int register, String newCard, int clientID) {
        this.register = register;
        this.newCard = newCard;
        this.clientID = clientID;
    }
}
