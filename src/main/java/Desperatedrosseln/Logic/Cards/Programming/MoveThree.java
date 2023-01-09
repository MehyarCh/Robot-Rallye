package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Robot;

public class MoveThree extends Programmingcard {

    @Override
    public void playCard(Robot robot) {
        robot.move(3);
    }
    @Override
    public String toString() {
        return "MoveThree";
    }

}

