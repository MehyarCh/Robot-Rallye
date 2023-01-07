package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Robot;

public class UTurn extends Programmingcard {

    @Override
    public void playCard(Robot robot) {
        robot.changeDirection(-180);
    }
    @Override
    public String toString() {
        return "UTurn";
    }
}
