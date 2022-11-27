package Server;

public class Antenna extends BoardElements{

    private Direction direction;
    private Position position;

    public Antenna(Direction direction, Position position) {
        super(position);
        this.direction = direction;
    }
}
