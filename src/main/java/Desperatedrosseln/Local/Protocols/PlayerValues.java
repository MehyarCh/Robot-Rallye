package Desperatedrosseln.Local.Protocols;

public class PlayerValues {

    /*
    Body:
        "name": "Nr. 5",
        "figure": 5
     */

    private String name;
    private int figure;

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }

    public PlayerValues(String name, int figure) {
        this.name = name;
        this.figure = figure;
    }
}
