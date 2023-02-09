package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Robot;

public class UTurn extends Programmingcard {
    private final int moves = 0;
    @Override
    public void playCard(Robot robot) {
        robot.changeDirection(-180);
    }
    @Override
    public String toString() {
        return "UTurn";
    }

    @Override
    public int getMoves(){
        return moves;
    }
}
