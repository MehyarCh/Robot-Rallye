package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class CurrentCards {

    /*
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

    private List<ActiveCards> activeCards = new ArrayList<ActiveCards>();

    public CurrentCards(List<ActiveCards> activeCards){
        this.activeCards = activeCards;
    }




    public static class ActiveCards {
        private int clientID;
        private String card;

        public int getClientID() {
            return clientID;
        }

        public String getCard() {
            return card;
        }

        public ActiveCards (int clientID, String card){
            this.clientID = clientID;
            this.card = card;
        }
    }
}
