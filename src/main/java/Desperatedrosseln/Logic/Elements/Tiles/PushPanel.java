package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.ArrayList;
import java.util.List;

public class PushPanel extends BoardElement {
    private ArrayList<String> orientations;
    private ArrayList<Integer> registers;
    private Position position;

    public PushPanel(String type, String isOnBoard, ArrayList<String> orientations, ArrayList<Integer> registers) {
        super(type,isOnBoard);
        this.orientations = orientations;
        this.registers = registers;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public ArrayList<Integer> getRegisters() {
        return registers;
    }

    public void execute(List<Desperatedrosseln.Logic.Elements.Robot> robotList) {
        for(Robot curr : robotList) {
            curr.push(orientations.get(0)); //first element of the orientations list in panel contains the direction in which the push panel pushes the robot
        }
    }
    @Override
    public String toString(){
        return "PushPanel";
    }
    @Override
    public Position getPosition(){
        return position;
    }
    @Override
    public void setPosition(int x, int y){
        if(position!=null){
            position.setX(x);
            position.setY(y);
        }else{
            position = new Position(x,y);
        }
    }


}
