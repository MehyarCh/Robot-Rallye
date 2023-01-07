package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Robot;

public class MoveBack extends Programmingcard {

    @Override
    public void playCard(Robot robot) {
        robot.moveBack();
    }
    @Override
    public String toString() {
        return "moveBack";
    }

}
