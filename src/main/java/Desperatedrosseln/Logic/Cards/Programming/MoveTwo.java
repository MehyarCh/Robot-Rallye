package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class MoveTwo extends Card {

    public MoveTwo(EventHandler<MouseEvent> event) {
        super("MoveTwo", "card--MoveTwo", event);
    }

    @Override
    public void playCard() {

    }
    @Override
    public String toString() {
        return "moveTwo";
    }
    public boolean isDamageCard(){
        return false;
    }
}
