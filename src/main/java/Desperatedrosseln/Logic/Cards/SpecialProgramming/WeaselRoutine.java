package Desperatedrosseln.Logic.Cards.SpecialProgramming;

import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class WeaselRoutine extends Card {
    public WeaselRoutine(EventHandler<MouseEvent> event) {
        super("WeaselRoutine", "card--WeaselRoutine", event);
    }

    @Override
    public void playCard() {

    }
    public boolean isDamageCard(){
        return false;
    }
}
