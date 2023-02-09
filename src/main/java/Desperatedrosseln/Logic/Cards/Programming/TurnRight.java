package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Robot;

public class TurnRight extends Programmingcard {
    private final int moves = 0;
    @Override
    public void playCard(Robot robot) {
        robot.changeDirection(-90);
    }
    @Override
    public String toString() {
        return "TurnRight";
    }

    @Override
    public int getMoves(){
        return moves;
    }
}

