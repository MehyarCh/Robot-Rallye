package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.*;

public class Again extends Card {

    public Again() {
        super("Again", "card--Again");
    }

    @Override
    public void playCard() {

    }
    public boolean isDamageCard(){
        return false;
    }

    @Override
    public String toString() {
        return "Again";
    }
}

