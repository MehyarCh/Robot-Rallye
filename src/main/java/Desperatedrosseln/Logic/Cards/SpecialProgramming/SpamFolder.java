package Desperatedrosseln.Logic.Cards.SpecialProgramming;

import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class SpamFolder extends Card {

    public SpamFolder(EventHandler<MouseEvent> event) {
        super("SpamFolder", "card--SpamFolder");
    }

    @Override
    public void playCard() {

    }
    public boolean isDamageCard(){
        return false;
    }
}
