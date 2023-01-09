package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

public class SelectedCard {

    /*
    Body:
        "card": "Again",
        "register": 5
     */

    private String card;
    private int register;

    public String getCard() {
        return card;
    }

    public int getRegister() {
        return register;
    }

    public SelectedCard(String card, int register) {
        this.card = card;
        this.register = register;
    }
}
