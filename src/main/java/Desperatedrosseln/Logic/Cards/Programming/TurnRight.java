package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class TurnRight extends Card {

    public TurnRight(EventHandler<MouseEvent> event) {
        super("TurnRight", "card--TurnRight", event);
    }

    @Override
    public void playCard() {

    }
    @Override
    public String toString() {
        return "TurnRight";
    }
    public boolean isDamageCard(){
        return false;
    }
}

