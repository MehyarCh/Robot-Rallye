package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

public class CardPlayed {

    /*
    Body:
        "clientID": 2,
        "card": "MoveI"
     */
    private Integer clientID;
    private Card card;

    public CardPlayed(Integer clientID, Card card) {
        this.clientID = clientID;
        this.card = card;
    }

    public Integer getClientID() {
        return clientID;
    }

    public Card getCard() {
        return card;

    }
}
