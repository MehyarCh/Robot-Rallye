package Desperatedrosseln.Local.Protocols;

import java.util.List;

public class SelectMap {

    /*
    Body: "availableMaps": ["DizzyHighway"] <- Liste
     */

    private List<String> maps;

    public List<String> getMaps() {
        return maps;
    }

    public SelectMap(List<String> maps) {
        this.maps = maps;
    }
}
