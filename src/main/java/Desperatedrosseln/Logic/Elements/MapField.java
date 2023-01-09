package Desperatedrosseln.Logic.Elements;

import java.util.List;

public class MapField {
    private List<BoardElement> types;

    public MapField(List<BoardElement> typeList) {
        this.types = typeList;
    }

    public List<BoardElement> getTypes() {
        return types;
    }

    public void setTypes(List<BoardElement> typeList) {
        this.types = typeList;
    }
}
