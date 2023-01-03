package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.*;

public class MoveThree extends Card {

    public MoveThree() {
        super("MoveThree", "card--MoveThree");
    }

    @Override
    public void playCard() {

    }
    @Override
    public String toString() {
        return "moveThree";
    }
    public boolean isDamageCard(){
        return false;
    }
}

