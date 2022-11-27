package Server;

abstract class BoardElements {

    private int priority;
    private Position position;

    public BoardElements(Position position) { //ToDo: priority (?)
        this.position = position;
    }

    public void execute(){

    }

    @Override
    public String toString(){
        return null;
    }
}
