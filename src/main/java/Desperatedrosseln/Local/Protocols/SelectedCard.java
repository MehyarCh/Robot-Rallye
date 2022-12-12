package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;

public class SelectedCard {

    /*
    Body:
        "card": "Again",
        "register": 5
     */

    private Card card;
    private int register;

    public Card getCard() {
        return card;
    }

    public int getRegister() {
        return register;
    }

    public SelectedCard(Card card, int register) {
        this.card = card;
        this.register = register;
    }
}
