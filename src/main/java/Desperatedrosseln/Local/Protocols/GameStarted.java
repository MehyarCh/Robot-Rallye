package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Elements.tiles.Tile;
import java.util.List;

public class GameStarted {
    private List<List<List<Tile>>> gameMap;
    public GameStarted(List<List<List<Tile>>> gameMap) {
        this.gameMap = gameMap;
    }

    public List<List<List<Tile>>> getGameMap() {
        return gameMap;
    }

    public void setGameMap(List<List<List<Tile>>> gameMap) {
        this.gameMap = gameMap;
    }

    public List<List<List<Tile>>> setTile(int x, int y, int z, Tile tile) {
        gameMap.get(x).get(y).add(z, tile);
        return gameMap;
    }

}
