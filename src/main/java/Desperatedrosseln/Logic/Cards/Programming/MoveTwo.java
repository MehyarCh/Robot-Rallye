package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Robot;

public class MoveTwo extends Programmingcard {

    @Override
    public void playCard(Robot robot) {
        robot.move(2);
    }
    @Override
    public String toString() {
        return "moveTwo";
    }

}
