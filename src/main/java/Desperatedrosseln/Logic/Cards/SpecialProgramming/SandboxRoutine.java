package Desperatedrosseln.Logic.Cards.SpecialProgramming;

import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class SandboxRoutine extends Card {
    public SandboxRoutine(EventHandler<MouseEvent> event) {
        super("SandboxRoutine", "card--SandboxRoutine");
    }

    @Override
    public void playCard() {

    }
    public boolean isDamageCard(){
        return false;
    }
}
