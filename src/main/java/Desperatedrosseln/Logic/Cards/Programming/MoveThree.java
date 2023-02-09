package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

public class MoveThree extends Programmingcard {
    private final int moves = 3;
    @Override
    public void playCard(Robot robot) {
        robot.move(3);
    }
    @Override
    public String toString() {
        return "MoveThree";
    }

    @Override
    public int getMoves(){
        return  moves;
    }
}

