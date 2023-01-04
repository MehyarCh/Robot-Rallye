package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Elements.Tile;

import java.util.ArrayList;
import java.util.List;

public class GameStarted {

    // Body enthält komplette Map mit speziellen FeldTypen und Orientation

    private List<List<List<Tile>>> map;

    public GameStarted(ArrayList<List<List<Tile>>> map){
        this.map = map;
    }

    public List<List<List<Tile>>> getMap() {
        return map;
    }
}
