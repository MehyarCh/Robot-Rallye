package Desperatedrosseln.Logic;

import Desperatedrosseln.Logic.Cards.*;
import Desperatedrosseln.Logic.Cards.Programming.*;


import java.util.*;

public class DeckHelper {

    //shuffle the given list (deck)
    public static List<Card> shuffleCards(List<Card> cards) {
        Collections.shuffle(cards);
        Collections.shuffle(cards);
        Collections.shuffle(cards);
        return cards;
    }

    //add cards from the discardPile back to the deck
    public static List<Card> rebuildDeck(List<Card> discarded, List<Card> deck) {

        for (int i = 0; i < discarded.size(); i++) {
            deck.add(discarded.get(i));
        }
        return shuffleCards(deck);

    }

    //build a deck of 20 cards for a player and shuffle it
    public static List<Card> buildDeck(List<Card> cards) {

        for (int i = 0; i < 5; i++) {
            cards.add(new MoveOne());
        }

        for (int i = 0; i < 3; i++) {
            cards.add(new MoveTwo());
            cards.add(new TurnRight());
            cards.add(new TurnLeft());
        }

        for (int i = 0; i < 2; i++) {
            cards.add(new Again());
        }

        cards.add(new UTurn());
        cards.add(new MoveBack());
        cards.add(new MoveThree());
        cards.add(new PowerUp());

        return (shuffleCards(cards));

    }


}
