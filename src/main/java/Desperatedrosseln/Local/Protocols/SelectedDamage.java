package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;
import Desperatedrosseln.Logic.Cards.Damagecard;

import java.util.List;

public class SelectedDamage {

    private List<Damagecard> cards;


    public SelectedDamage(List<Damagecard> cards) {
        this.cards = cards;
    }

    public List<Damagecard> getCards() {
        return cards;
    }
}
