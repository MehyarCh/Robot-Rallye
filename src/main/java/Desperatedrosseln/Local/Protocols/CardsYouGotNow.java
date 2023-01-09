package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class CardsYouGotNow {

    /*
    Body:
        "cards": [
            "card1",
            "..."
     */
    private List<String> cards;

    public List<String> getCards() {
        return cards;
    }

    public CardsYouGotNow (List<String> cards){
        this.cards = cards;
    }

}
