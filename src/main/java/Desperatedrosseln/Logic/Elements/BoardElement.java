package Desperatedrosseln.Logic.Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BoardElement {

    private int priority;
    private Position position;

    private ArrayList<String> orientations;

    public void execute(){

    }
    public void execute(List<Robot> active_robots){

    }

    @Override
    public String toString(){
        return null;
    }

    public Position getPosition(){
        return this.position;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

}

