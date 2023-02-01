package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

public class PlayCard {

    //Body: "card": "MoveI"
    private String card;

    public String getCard() {
        return card;
    }

    public PlayCard (String card){
        this.card = card;
    }
}
