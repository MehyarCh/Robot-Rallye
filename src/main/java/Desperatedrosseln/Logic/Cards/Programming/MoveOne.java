package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.*;
import Desperatedrosseln.Logic.Cards.Programmingcard;

public class MoveOne extends Programmingcard {

    @Override
    public void playCard(Player player) {
        player.getRobot().move(1);
    }

    @Override
    public String toString() {
        return "moveOne";
    }
    public boolean isDamageCard(){
        return false;
    }
}

