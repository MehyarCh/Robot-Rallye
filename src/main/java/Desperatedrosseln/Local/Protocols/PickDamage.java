package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Cards.Card;
import Desperatedrosseln.Logic.Cards.Damagecard;

import java.util.List;


public class PickDamage {

    private int count;

    private List<String> availablePiles;


    public int getCount() {
        return count;
    }

    public List<String> getAvailablePiles() {
        return availablePiles;
    }

    public PickDamage(int count, List<String> availablePiles) {
        this.count = count;
        this.availablePiles = availablePiles;
    }
}
