package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.*;
import Desperatedrosseln.Logic.Cards.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;

import java.util.*;

public class MoveOne extends Card {


    public MoveOne(EventHandler<MouseEvent> event) {
        super("MoveOne", "card--MoveOne", event);
    }

    @Override
    public void playCard(Player player) {
        player.getRobot().move(1);
    }

    @Override
    public void playCard() {

    }


    public boolean isDamageCard(){
        return false;
    }

}

