package Desperatedrosseln.Logic.Elements;

import java.util.ArrayList;
import java.util.List;

public class Map {
    List<List<MapField>> mapFields;

    public Map(List<List<MapField>> mapFields) {
        this.mapFields = mapFields;
    }

    public List<List<MapField>> getMapFields() {
        return mapFields;
    }

    public void setMapFields(List<List<MapField>> mapFields) {
        this.mapFields = mapFields;
    }

    public List<Robot> getRobotsOnPos(Position pos){
        List<Robot> robots = new ArrayList<>();
        List<BoardElement> elements = getElementsOnPos(pos);

        for(BoardElement element : elements){
            if( element.toString().equals("Robot")){
                Robot robot = (Robot) element;
                robots.add(robot);
            }
        }
        return robots;
    }
    public void addElement(BoardElement element, int x, int y){
      mapFields.get(x).get(y).getTypes().add(element);
    }

    public int getLength(){
        return mapFields.get(0).size();
    }
    public int getWidth(){
        return mapFields.size();
    }

    public List<BoardElement> getElementsOnPos(Position pos) {
        return mapFields.get(pos.getX()).get(pos.getY()).getTypes();
    }
}
