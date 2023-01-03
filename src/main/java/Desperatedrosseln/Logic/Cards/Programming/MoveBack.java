package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.*;

public class MoveBack extends Card {

    public MoveBack() {
        super("MoveBack", "card--MoveBack");
    }

    @Override
    public void playCard() {
        //case up, down, left, right
    }
    @Override
    public String toString() {
        return "MoveBack";
    }
    public boolean isDamageCard(){
        return false;
    }
}
