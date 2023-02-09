package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.Tiles.Wall;

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

    /**
     * removes robot from old position and adds it to new position
     * @param robot
     * @param oldpos
     * @param newpos
     * @return true if robot moved correctly
     */
    public boolean moveRobot(Robot robot, Position oldpos, Position newpos) {
        List<BoardElement> elements = mapFields.get(oldpos.getX()).get(oldpos.getY()).getTypes();
        boolean removed = elements.remove(robot);
        if(removed){
            mapFields.get(newpos.getX()).get(newpos.getY()).getTypes().add(robot);
            return true;
        }
        //robot not removed from old pos
        return false;
    }

    public boolean hasRobotOnPos(Position position){
        List<BoardElement> elements = mapFields.get(position.getX()).get(position.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("Robot")){
                return true;
            }
        }
        return false;
    }

    /**
     * @param pos position to check
     * @return true if there is an antenna or wall on position pos
     */
    public boolean hasAntenna(Position pos){
        List<BoardElement> elements = mapFields.get(pos.getX()).get(pos.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("Antenna")){
                return true;
            }
        }
        return false;
    }
    public boolean nextPosHasBlockingWall(Position pos, DIRECTION dir){
        List<BoardElement> elements = mapFields.get(pos.getX()).get(pos.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("Wall")){
                Wall wall = (Wall) element;
                if(wall.getOrientations().get(0).equals(DIRECTION.getOppositeOf(dir).toString())){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean currPosHasBlockingWall(Position pos, DIRECTION dir){
        List<BoardElement> elements = mapFields.get(pos.getX()).get(pos.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("Wall")){
                Wall wall = (Wall) element;
                if(wall.getOrientations().get(0).equals(dir.toString())){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPit(Position pos) {
        List<BoardElement> elements = mapFields.get(pos.getX()).get(pos.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("Pit")){
                return true;
            }
        }
        return false;
    }
}
