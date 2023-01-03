package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.*;

public class UTurn extends Card {

    public UTurn() {
        super("UTurn", "card--UTurn");
    }

    @Override
    public void playCard() {

    }
    @Override
    public String toString() {
        return "UTurn";
    }
    public boolean isDamageCard(){
        return false;
    }
}
