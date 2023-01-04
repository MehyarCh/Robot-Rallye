package Desperatedrosseln.Logic.Elements;

import java.util.*;

public class GameMap {

    public List<List<List<Tile>>> getGameMap() {
        return gameMap;
    }

    private List<List<List<Tile>>> gameMap;

    public GameMap(){
        gameMap = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            gameMap.add(new ArrayList<>());
            for (int j = 0; j < 10; j++){
                gameMap.get(i).add(new ArrayList<>());
            }
        }

        List<List<List<? super Tile>>> gm;
        gm = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            gm.add(new ArrayList<>());
            for (int j = 0; j < 10; j++){
                gm.get(i).add(new ArrayList<>());
            }
        }
        Position p = new Position(1,1);
      //  Tile b = new Tile();
       // Empty e = new Empty();
      //  gm.get(p.getX()).get(p.getY()).add(e);

        List<?> x;
    }
    public void setTile(Position p, Tile b){
        gameMap.get(p.getX()).get(p.getY()).add(b);
    }

}
