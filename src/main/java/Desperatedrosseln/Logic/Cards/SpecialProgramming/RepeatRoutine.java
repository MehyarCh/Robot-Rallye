package Desperatedrosseln.Logic.Cards.SpecialProgramming;

import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class RepeatRoutine extends Card {
    public RepeatRoutine(EventHandler<MouseEvent> event) {
        super("RepeatRoutine", "card--RepeatRoutine");
    }

    @Override
    public void playCard() {

    }
    public boolean isDamageCard(){
        return false;
    }
}
