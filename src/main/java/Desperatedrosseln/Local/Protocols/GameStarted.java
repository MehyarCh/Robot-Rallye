package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Elements.BoardElement;
import java.util.List;

public class GameStarted {
    private List<List<List<BoardElement>>> gameMap;
    public GameStarted(List<List<List<BoardElement>>> gameMap) {
        this.gameMap = gameMap;
    }

    public List<List<List<BoardElement>>> getGameMap() {
        return gameMap;
    }

    public void setGameMap(List<List<List<BoardElement>>> gameMap) {
        this.gameMap = gameMap;
    }

    public List<List<List<BoardElement>>> setTile(int x, int y, int z, BoardElement boardElement) {
        gameMap.get(x).get(y).add(z, boardElement);
        return gameMap;
    }

}
