package Desperatedrosseln.Logic.Elements;

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
}
