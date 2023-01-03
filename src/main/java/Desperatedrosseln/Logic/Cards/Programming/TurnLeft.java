package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.*;

public class TurnLeft extends Card {

    public TurnLeft() {
        super("TurnLeft", "card--TurnLeft");
    }

    @Override
    public void playCard() {

    }

    public boolean isDamageCard(){
        return false;
    }
}

