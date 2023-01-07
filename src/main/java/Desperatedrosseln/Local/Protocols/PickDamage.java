package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;
import Desperatedrosseln.Logic.Cards.Damagecard;

import java.util.List;


public class PickDamage {

    private int count;

    private List<Damagecard> availablePiles;


    public int getCount() {
        return count;
    }

    public List<Damagecard> getAvailablePiles() {
        return availablePiles;
    }

    public PickDamage(int count, List<Damagecard> availablePiles) {
        this.count = count;
        this.availablePiles = availablePiles;
    }
}
