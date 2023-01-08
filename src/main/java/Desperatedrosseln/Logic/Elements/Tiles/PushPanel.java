package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.ArrayList;
import java.util.List;

public class PushPanel extends BoardElement {

 ArrayList<String> orientations;
 private ArrayList<Integer> registers;




    public PushPanel(String type, String isOnBoard, ArrayList<String> orientations) {
        super(type,isOnBoard);
        this.orientations = orientations;

    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public ArrayList<Integer> getRegisters() {
        return registers;
    }

    public void execute(List<Desperatedrosseln.Logic.Elements.Robot> robotList) {
        //TODO
        for(Robot curr : robotList) {
            curr.push(orientations.get(0)); //first element of the orientations list in panel contains the direction in which the push panel pushes the robot
        }
    }


}
