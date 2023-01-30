package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class DrawDamage {

    private int clientID;
    private List<String> cards;


    public int getClientID() {
        return clientID;
    }

    public List<String> getCards() {
        return cards;
    }

    public DrawDamage(int clientID, List<String> cards) {
        this.clientID = clientID;
        this.cards = cards;
    }
}
