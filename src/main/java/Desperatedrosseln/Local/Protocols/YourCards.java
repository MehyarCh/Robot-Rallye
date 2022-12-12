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

    private ArrayList<Card> cardsInHand;

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public YourCards(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }
}
