package Desperatedrosseln.Logic;

import Desperatedrosseln.Logic.Cards.*;
import Desperatedrosseln.Logic.Cards.Programming.*;


import java.util.*;

public class DeckHelper {

    //shuffle the given list (deck)
    public static ArrayList<Card> shuffleCards(ArrayList<Card> cards){
        Collections.shuffle(cards);
        return cards;
    }

    //add cards from the discardPile back to the deck
    public static ArrayList<Card> rebuildDeck(ArrayList<Card> discarded, ArrayList<Card> deck){

        for (int i = 0; i < discarded.size(); i++){
            deck.add(discarded.get(i));
        }
        return shuffleCards(deck);
        
    }

    //build a deck of 20 cards for a player and shuffle it
    public static ArrayList<Card> buildDeck(ArrayList<Card> cards){

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

        for (int i = 0; i < 1; i++) {
            cards.add(new UTurn());
            cards.add(new MoveBack());
            cards.add(new MoveThree());

        }
        return(shuffleCards(cards));

    }



}
