package Desperatedrosseln.Local.CardLabels;

import Desperatedrosseln.Local.*;
import javafx.event.*;
import javafx.scene.input.*;

public class TurnLeftLabel extends CardLabel {

    public TurnLeftLabel(EventHandler<MouseEvent> event) {
        super("MoveOne", "card--TurnLeft", event);
    }
}
