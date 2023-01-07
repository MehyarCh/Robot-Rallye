package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Robot;

public class MoveOne extends Programmingcard {

    @Override
    public void playCard(Robot robot) {
        robot.move(1);
    }

    @Override
    public String toString() {
        return "moveOne";
    }

}

