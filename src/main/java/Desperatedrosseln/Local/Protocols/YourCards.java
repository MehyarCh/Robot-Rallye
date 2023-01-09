package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class YourCards {

    /*
    Body:
        "cardsInHand": [
        "card1",
        "..."
     */

    private List<String> cardsInHand;

    public List<String> getCardsInHand() {
        return cardsInHand;
    }

    public YourCards(List<String> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }
}
