package Server;

public class Robot extends BoardElements{

    private Direction direction;
    private Position position;

    public Robot(Direction direction, Position position) { //ToDo: verify
        super(position);
        this.direction = direction;
    }

    @Override
    public void execute(){

    }

    public void move(){

    }

    private void push(){

    }
}
