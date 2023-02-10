package Desperatedrosseln.Logic.AI;

import Desperatedrosseln.Logic.Elements.Position;
/**
 * @author Rishabh
 *
 * nodes for path finding algorithm
 *
 */
public class Node {
    Node prev;
    int g;
    int h;
    Position pos;
    boolean walkable = false;
    Node(Position pos) {
        this.pos = pos;
    }
    public void setPrev(Node prev) {
        this.prev = prev;
    }
    public int getF() {
        return g + h;
    }
    public Position getPos() {
        return pos;
    }


}
