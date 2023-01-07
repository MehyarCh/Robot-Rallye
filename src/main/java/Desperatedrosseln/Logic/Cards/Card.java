package Desperatedrosseln.Logic.Cards;

import Desperatedrosseln.Logic.Elements.Robot;

public class Card {
    private String info;

    /*Konstruktor?
    public Card(String info) {
        this.info = info;
    }*/

    public void playCard(Robot robot){
    }

    public String toString(){
        return "Card";
    }

    public boolean isAllowedMove(){
        //conditions in which a move is allowed/not allowed (moving from a pit == false)
        return true;
    }
    public String getInfo() {
        return info;
    }
    public boolean isDamageCard(){
        return false;
    }

}

