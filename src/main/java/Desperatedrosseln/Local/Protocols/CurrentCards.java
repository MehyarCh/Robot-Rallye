package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;

public class CurrentCards {

    /*
    ToDo:
    Body:
        "activeCards": [
            {
            "clientID": 1,
            "card": "MoveI"
            },
            {
            "clientID": 2,
            "card": "Spam"
            }
        ]
     */
    private ArrayList<ActiveCards> activeCards = new ArrayList<ActiveCards>();

    public CurrentCards(ArrayList<ActiveCards> activeCards){
        this.activeCards = activeCards;
    }

    public static class ActiveCards {
        private int clientID;
        private Card card;

        public int getClientID() {
            return clientID;
        }

        public Card getCard() {
            return card;
        }

        public ActiveCards (int clientID, Card card){
            this.clientID = clientID;
            this.card = card;
        }
    }
}
