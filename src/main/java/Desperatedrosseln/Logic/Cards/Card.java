package Desperatedrosseln.Logic.Cards;

import Desperatedrosseln.Logic.*;

public abstract class Card {


    private String info;

    /*Konstruktor?
    public Card(String info) {
        this.info = info;
    }*/

    public void playCard(Player player){

    }

    public String toString(){
        return "ll";
    }

    public boolean isAllowedMove(){
        //conditions in which a move is allowed/not allowed (moving from a pit == false)
        return true;
    }
    public String getInfo() {
        return info;
    }

}

