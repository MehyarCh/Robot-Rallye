package Desperatedrosseln.Logic.Elements;

import javafx.geometry.Pos;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PushPanel extends BoardElement{

 ArrayList<String> orientations;
 private ArrayList<Integer> registers;

 private Position position;


    public PushPanel(Position position, String orientation, ArrayList<Integer> registers) {

        this.position = position;
        this.orientations = new ArrayList<>();
        orientations.add(orientation);

        this.registers = registers;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public ArrayList<Integer> getRegisters() {
        return registers;
    }
    @Override
    public void execute(List<Robot> robotList) {
        //TODO
        for(Robot curr : robotList) {
            curr.push(orientations.get(0)); //first element of the orientations list in panel contains the direction in which the push panel pushes the robot
        }
    }
}
