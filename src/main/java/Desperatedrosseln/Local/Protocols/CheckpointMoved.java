package Desperatedrosseln.Local.Protocols;

public class CheckpointMoved {

    private int checkpointID;

    public int getCheckpointID() {
        return checkpointID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int x;
    private int y;

    public CheckpointMoved(int checkpointID, int x, int y){
        this.checkpointID = checkpointID;
        this.x = x;
        this.y = y;
    }
}

