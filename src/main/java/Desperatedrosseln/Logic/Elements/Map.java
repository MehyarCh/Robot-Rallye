package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.Tiles.ConveyorBelt;
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
    public boolean nextPosHasBlockingWall(Position robotpos, Position newposition){
        List<BoardElement> elements = mapFields.get(newposition.getX()).get(newposition.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("Wall")){
                Wall wall = (Wall) element;
                if( wallBlocksMovement(robotpos, wall)){
                    return true;
                }
                //if the wall block the movement then return
            }
        }
        return false;
    }

    private boolean wallBlocksMovement(Position robotpos, Wall wall){
        //supposedly wall position
        int newx = wall.getPosition().getX();
        int newy = wall.getPosition().getY();
        int i = robotpos.getX() - wall.getPosition().getX();
        int j = robotpos.getY() - wall.getPosition().getY();

        if(i == 0){
            //same column
            if(j>0){
                //robot coming from bot
                if(wall.getOrientations().get(0).equals(DIRECTION.BOTTOM.toString())){
                    return true;
                }
            }else if(j<0){
                //robot coming from top
                if(wall.getOrientations().get(0).equals(DIRECTION.TOP.toString())){
                    return true;
                }
            }
        }else if (j == 0){
            //same line
            if(i>0){
                //robot coming from right
                if(wall.getOrientations().get(0).equals(DIRECTION.RIGHT.toString())){
                    return true;
                }
            }else if(i<0){
                //robot coming from left, push to right
                if(wall.getOrientations().get(0).equals(DIRECTION.LEFT.toString())){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean currPosHasBlockingWall(Position pos, Position newposition){
        List<BoardElement> elements = mapFields.get(pos.getX()).get(pos.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("Wall")){
                Wall wall = (Wall) element;
                if( wallOnPosBlocksMovement(wall, newposition)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean wallOnPosBlocksMovement(Wall wall, Position newposition ){
        //supposedly wall position
        int newx = wall.getPosition().getX();
        int newy = wall.getPosition().getY();
        int i = newposition.getX() - wall.getPosition().getX();
        int j = newposition.getY() - wall.getPosition().getY();

        if(i == 0){
            //same column
            if(j>0){
                //robot going bot
                if(wall.getOrientations().get(0).equals(DIRECTION.BOTTOM.toString())){
                    return true;
                }
            }else if(j<0){
                //robot going top
                if(wall.getOrientations().get(0).equals(DIRECTION.TOP.toString())){
                    return true;
                }
            }
        }else if (j == 0){
            //same line
            if(i>0){
                //robot going right
                if(wall.getOrientations().get(0).equals(DIRECTION.RIGHT.toString())){
                    return true;
                }
            }else if(i<0){
                //robot going left
                if(wall.getOrientations().get(0).equals(DIRECTION.LEFT.toString())){
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
    public boolean hasBlueConveyorBelt(Position pos) {
        List<BoardElement> elements = mapFields.get(pos.getX()).get(pos.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("ConveyorBelt")){
                ConveyorBelt belt = (ConveyorBelt) element;
                if(belt.getSpeed() == 2 ){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasGreenConveyorBelt(Position pos) {
        List<BoardElement> elements = mapFields.get(pos.getX()).get(pos.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("ConveyorBelt")){
                ConveyorBelt belt = (ConveyorBelt) element;
                if( belt.getSpeed() == 1){
                    return true;
                }
            }
        }
        return false;
    }

    public ConveyorBelt getConveyorBeltOnPos(Position pos){
        List<BoardElement> elements = mapFields.get(pos.getX()).get(pos.getY()).getTypes();
        for(BoardElement element : elements){
            if(element.toString().equals("ConveyorBelt")){
                ConveyorBelt belt = (ConveyorBelt) element;
                return belt;
            }
        }
        return null;
    }

    public DIRECTION robotIsComingFrom(Position elementpos, Position robotpos){
        int i = robotpos.getX() - elementpos.getX();
        int j = robotpos.getY() - elementpos.getY();

        if(i == 0){
            //same column
            if(j>0){
                //robot going bot
                return DIRECTION.BOTTOM;
            }else if(j<0){
                //robot going top
                return DIRECTION.TOP;
            }
        }else if (j == 0){
            //same line
            if(i>0){
                //robot going right
                return DIRECTION.RIGHT;
            }else if(i<0){
                //robot going left
                return DIRECTION.LEFT;
            }
        }
        return null;
    }
}
