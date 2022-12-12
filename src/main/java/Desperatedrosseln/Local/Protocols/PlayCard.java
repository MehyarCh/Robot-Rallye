package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

public class PlayCard {

    //Body: "card": "MoveI"
    private Card card;

    public Card getCard() {
        return card;
    }

    public PlayCard (Card card){
        this.card = card;
    }
}
