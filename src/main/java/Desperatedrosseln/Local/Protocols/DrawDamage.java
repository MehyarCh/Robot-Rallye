package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;

public class DrawDamage {

    private int clientID;
    private ArrayList<Card> cards;


    public int getClientID() {
        return clientID;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public DrawDamage(int clientID, ArrayList<Card> cards) {
        this.clientID = clientID;
        this.cards = cards;
    }
}
