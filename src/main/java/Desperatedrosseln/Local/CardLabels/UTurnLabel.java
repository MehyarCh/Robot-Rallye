package Desperatedrosseln.Local.CardLabels;

import Desperatedrosseln.Local.*;
import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.input.*;

public class UTurnLabel extends CardLabel {

    public UTurnLabel(EventHandler<MouseEvent> event) {
        super("MoveOne", "card--UTurn", event);
    }
}
