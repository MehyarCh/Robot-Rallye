package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

public class CardPlayed {

    /*
    Body:
        "clientID": 2,
        "card": "MoveI"
     */
    private Integer clientID;
    private String card;

    public CardPlayed(Integer clientID, String card) {
        this.clientID = clientID;
        this.card = card;
    }

    public Integer getClientID() {
        return clientID;
    }

    public String getCard() {
        return card;

    }
}
