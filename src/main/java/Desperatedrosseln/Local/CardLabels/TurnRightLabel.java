package Desperatedrosseln.Local.CardLabels;

import Desperatedrosseln.Local.*;
import javafx.event.*;
import javafx.scene.input.*;

public class TurnRightLabel extends CardLabel {

    public TurnRightLabel(EventHandler<MouseEvent> event) {
        super("MoveOne", "card--TurnRight", event);
    }
}
