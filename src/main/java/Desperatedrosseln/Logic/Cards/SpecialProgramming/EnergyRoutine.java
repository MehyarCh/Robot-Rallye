package Desperatedrosseln.Logic.Cards.SpecialProgramming;

import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class EnergyRoutine extends Card {

    public EnergyRoutine(EventHandler<MouseEvent> event) {
        super("EnergyRoutine", "card--EnergyRoutine", event);
    }

    @Override
    public void playCard() {

    }
    public boolean isDamageCard(){
        return false;
    }
}
