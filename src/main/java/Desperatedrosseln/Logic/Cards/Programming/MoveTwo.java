package Desperatedrosseln.Logic.Cards.Programming;

import Desperatedrosseln.Logic.Cards.Programmingcard;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

public class MoveTwo extends Programmingcard {
    private final int moves = 2;
    @Override
    public void playCard(Robot robot) {
        robot.move(2);
    }
    @Override
    public String toString() {
        return "MoveTwo";
    }

    @Override
    public int getMoves(){
        return  moves;
    }

}
