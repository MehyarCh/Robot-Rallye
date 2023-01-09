package Desperatedrosseln.Local.CardLabels;

import Desperatedrosseln.Local.*;
import Desperatedrosseln.Logic.Cards.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
import javafx.event.*;
import javafx.scene.input.*;

public class PowerUpLabel extends CardLabel {

    public PowerUpLabel(EventHandler<MouseEvent> event) {
        super("MoveOne", "card--PowerUp", event);
    }
}
