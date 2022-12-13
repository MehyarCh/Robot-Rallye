package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;

public class MoveBack extends Programmingcard {

    @Override
    public void playCard() {
        //case up, down, left, right
    }
    @Override
    public String toString() {
        return "moveBack";
    }
    public boolean isDamageCard(){
        return false;
    }
}
