package Desperatedrosseln.Logic.Cards;

import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

public class Programmingcard extends Card{

    /*public Programmingcards(String info) {
        super(info);
    }*/

    public void playCard(){

    }
    public void playCard(Robot robot) {
        //move robot

    }

    public Position calculateNewPosition(Robot robot){
        DIRECTION dir = robot.getDirection();
        Position position = robot.getPosition();
        int x = position.getX();
        int y = position.getY();
        switch (dir){
            case TOP -> {
                y = y - 1;
            }
            case BOTTOM -> {
                y = y + 1;
            }
            case LEFT -> {
                x = x - 1;
            }
            case RIGHT -> {
                x = x + 1;
            }
            default -> {

            }
        }
        return new Position(x,y);
    }

    @Override
    public String toString(){

        return "ProgrammingCard";
    }
    public int getMoves(){
        return 0;
    }
}

