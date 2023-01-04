package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;

public class YourCards {

    /*
    Body:
        "cardsInHand": [
        "card1",
        "..."
     */

    private ArrayList<String> cardsInHand;

    public ArrayList<String> getCardsInHand() {
        return cardsInHand;
    }

    public YourCards(ArrayList<String> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }
}
