package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.Player;

import java.util.List;

public class Checkpoint extends BoardElement {
    private Position position;
    private int number;
    public Checkpoint(Position position, int number){
        this.position = position;
        this.number = number;
    }

    @Override
    public void execute(List<Robot> active_robots){

    }
    public void execute(Player player){
        if(player.getNextCheckPoint() == this.number){
            player.setNextCheckPoint();
        }
    }

    @Override
    public String toString(){
        return "Checkpoint";
    }
}

