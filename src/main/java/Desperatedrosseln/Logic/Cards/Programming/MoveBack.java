package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

public class MoveBack extends Programmingcard {
    private final int moves = 1;

    @Override
    public void playCard(Robot robot) {
        robot.moveBack();
    }
    @Override
    public String toString() {
        return "MoveBack";
    }

    @Override
    public int getMoves(){
        return  moves;
    }


}
