package Desperatedrosseln.Logic.Cards.SpecialProgramming;

import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class SpeedRoutine extends Card {
    public SpeedRoutine(EventHandler<MouseEvent> event) {
        super("SpeedRoutine", "card--SpeedRoutine");
    }

    @Override
    public void playCard() {

    }
    public boolean isDamageCard(){
        return false;
    }
}
