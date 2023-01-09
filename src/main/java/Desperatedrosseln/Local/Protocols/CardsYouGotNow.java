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
    private ArrayList<String> cards;

    public ArrayList<String> getCards() {
        return cards;
    }

    public CardsYouGotNow (ArrayList<String> cards){
        this.cards = cards;
    }

}
