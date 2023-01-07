package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Robot;

public class TurnLeft extends Programmingcard {

    @Override
    public void playCard(Robot robot) {
        robot.changeDirection(90);
    }
    @Override
    public String toString() {
        return "turnLeft";
    }

}

