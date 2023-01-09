package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.*;

/**
 * first dimension is the vertical axis
 * second is horizontal
 * going up means x - 1
 * going left means y -1
 */
public class GameMap {

    public List<List<List<BoardElement>>> getGameMap() {
        return gameMap;
    }

    private List<List<List<BoardElement>>> gameMap;

    public GameMap(){
        gameMap = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            gameMap.add(new ArrayList<>());
            for (int j = 0; j < 10; j++){
                gameMap.get(i).add(new ArrayList<>());
            }
        }
    }
    public void setTile(Position p, BoardElement b){
        gameMap.get(p.getX()).get(p.getY()).add(b);
    }
    public List<BoardElement> getElementsOnPos(Position pos){
        return gameMap.get(pos.getX()).get(pos.getY());
    }
    public List<Desperatedrosseln.Logic.Elements.Robot> getRobotsOnPos(Position pos){
        List<Desperatedrosseln.Logic.Elements.Robot> robots = new ArrayList<>();
        List<BoardElement> elements = getElementsOnPos(pos);
        for(BoardElement element : elements){
            if(element instanceof Desperatedrosseln.Logic.Elements.Robot){
                robots.add((Robot) element);
            }
        }
        return robots;
    }

    public int getLength(){
        return gameMap.size();
    }
    public int getWidth(){
        return gameMap.get(0).size();
    }

}
