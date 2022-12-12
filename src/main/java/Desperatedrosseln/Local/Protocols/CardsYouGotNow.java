package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;

public class CardsYouGotNow {

    /*
    Body:
        "cards": [
            "card1",
            "..."
     */
    private ArrayList<Card> cards;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public CardsYouGotNow (ArrayList<Card> cards){
        this.cards = cards;
    }

}
