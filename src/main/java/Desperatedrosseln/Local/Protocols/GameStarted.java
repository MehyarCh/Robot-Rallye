package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Elements.BoardElement;

import java.util.ArrayList;
import java.util.List;

public class GameStarted {

    // Body enthält komplette Map mit speziellen FeldTypen und Orientation

    private List<List<List<BoardElement>>> map;

    public GameStarted(ArrayList<List<List<BoardElement>>> map){
        this.map = map;
    }
}
