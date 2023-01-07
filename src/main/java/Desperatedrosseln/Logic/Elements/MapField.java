package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.Elements.tiles.Tile;
import java.util.List;

public class MapField {
    private List<Tile> types;

    public MapField(List<Tile> typeList) {
        this.types = typeList;
    }

    public List<Tile> getTypes() {
        return types;
    }

    public void setTypes(List<Tile> typeList) {
        this.types = typeList;
    }
}
