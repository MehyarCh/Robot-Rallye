package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;

public class DrawDamage {

    private int clientID;
    private ArrayList<String> cards;


    public int getClientID() {
        return clientID;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public DrawDamage(int clientID, ArrayList<String> cards) {
        this.clientID = clientID;
        this.cards = cards;
    }
}
