package Desperatedrosseln.Logic.Elements;

import java.util.*;

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

}
