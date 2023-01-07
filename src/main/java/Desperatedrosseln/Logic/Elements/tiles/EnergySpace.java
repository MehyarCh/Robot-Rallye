package Desperatedrosseln.Logic.Elements.tiles;

public class EnergySpace extends Tile {

    private int count;

    public EnergySpace(String type, String isOnBoard, int count) {
        super(type, isOnBoard);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
