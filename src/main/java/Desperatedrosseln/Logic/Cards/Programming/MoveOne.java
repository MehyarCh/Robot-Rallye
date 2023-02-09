package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

public class MoveOne extends Programmingcard {

    private final int moves = 1;
    @Override
    public void playCard(Robot robot) {
        robot.move(1);
    }

    @Override
    public String toString() {
        return "MoveOne";
    }

    @Override
    public int getMoves(){
        return  moves;
    }

}

