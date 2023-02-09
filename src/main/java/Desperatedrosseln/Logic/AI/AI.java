package Desperatedrosseln.Logic.AI;

import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;
import Desperatedrosseln.Logic.Elements.Tiles.CheckPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AI {
    int activePhase;
    int currentPlayer;
    int ID;
    Robot robot;
    private List<List<List<BoardElement>>> gameMap;
    private List<String> cardsInHand;
    ArrayList<Node> path;
    ArrayList<Robot> robotList = new ArrayList<>();

    public AI(int ai_id) {
        ID = ai_id;
    }

    public void setActivePhase(int activePhase) {
        this.activePhase = activePhase;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void setCardsInHand(List<String> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    int currentGoal = 1;

    public List<String> getCardsInHand() {
        return cardsInHand;
    }

    public String getRandomCard() {
        if (cardsInHand.size() == 0 || cardsInHand == null) {
            return "error";
        }
        int index = (int) Math.floor(Math.random() * (cardsInHand.size()));
        if (index == cardsInHand.size()) {
            --index;
        }
        String card = cardsInHand.get(index);
        cardsInHand.remove(index);
        return card;
    }

    public void setGameMap(List<List<List<BoardElement>>> gameMap) {
        this.gameMap = gameMap;
    }

    public ArrayList<String> selectRegisterCards() {
        //ToDo: increase current Goal by creating a checkpoint arraylist

        /*
        //testing
        List<List<List<BoardElement>>> gameMap1 = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
            gameMap1.add(new ArrayList<>());
            for (int j = 0; j < 10; j++) {
                gameMap1.get(i).add(new ArrayList<>());
                BoardElement element = new Empty("Empty", "");
                if (i == 8 && j == 8) {
                    element = new CheckPoint("CheckPoint", "", 0);
                }
                element.setPosition(i, j);
                gameMap1.get(i).get(j).add(element);
            }
        }
        gameMap = gameMap1;
        Robot robot1 = new Robot(8);
        robot1.setPosition(1, 1);
        gameMap.get(1).get(1).add(robot1);
        */

        Node[][] nodeMap = new Node[gameMap.get(0).size()][gameMap.size()];

        ArrayList<Node> openSet = new ArrayList<>();
        ArrayList<Node> closedSet = new ArrayList<>();

        Node startNode = null;
        Node checkPoint = null;

        for (int x = 0; x < gameMap.size(); x++) {
            for (int y = 0; y < gameMap.get(x).size(); y++) {
                Node curr = new Node(new Position(x, y));
                nodeMap[y][x] = curr;
                List<BoardElement> boardElements = gameMap.get(x).get(y);
                if (boardElements.size() == 1) {                                                                              //ToDO

                    if (checkElement("Empty", boardElements) || checkElement("ConveyorBelt", boardElements) || checkElement("StartPoint", boardElements) || checkElement("CheckPoint", boardElements)) {
                        curr.walkable = true;
                    }
                } else {

                }

                for (Robot rob :
                        robotList) {
                    if (boardElements.contains(rob)) {                                                                         //adding robots to nodes
                        if (rob == robot) {
                            startNode = curr;
                        } else {
                            curr.walkable = false;
                        }
                    }
                }


                if (checkElement("CheckPoint", gameMap.get(x).get(y))) {

                    boolean flag = false;                                       //Flag to check if the Checkpoint is currentGoal

                    for (BoardElement element : gameMap.get(x).get(y)) {
                        if (element.getType().equals("CheckPoint")) {
                            CheckPoint checkPoint1 = (CheckPoint) element;
                            if (checkPoint1.getCount() == currentGoal) {
                                flag = true;
                            }
                            break;
                        }
                    }
                    if (flag) {
                        curr.walkable = true;
                        checkPoint = curr;
                    }

                }

            }
        }
        /*
        for (int x = 0; x < nodeMap.length; x++) {
            for (int y = 0; y < nodeMap[x].length; y++) {
                Node node = nodeMap[x][y];
                if (node.walkable) {
                    if (node == checkPoint) {
                        continue;
                    }
                    if (node == startNode) {
                        continue;
                    }
                } else {
                }
            }
        }
        */
        openSet.add(startNode);

        while (openSet.size() > 0) {

            Node curr = openSet.get(0);

            for (int i = 1; i < openSet.size(); i++) {
                if (curr.getF() > openSet.get(i).getF() || curr.getF() == openSet.get(i).getF()) {
                    if (curr.h > openSet.get(i).h) {
                        curr = openSet.get(i);
                    }
                }
            }
            closedSet.add(curr);
            openSet.remove(curr);

            if (curr == checkPoint) {

                path = retracePath(startNode, checkPoint);
                break;
            }

            for (Node neighbour :
                    getNeighbours(curr, nodeMap)) {

                if (!neighbour.walkable || closedSet.contains(neighbour)) {
                    continue;
                }

                int newMovementCost = curr.g + getDistanceBetweenNodes(curr, neighbour);

                if (newMovementCost < neighbour.g || !openSet.contains(neighbour)) {
                    neighbour.g = newMovementCost;
                    neighbour.h = getDistanceBetweenNodes(neighbour, checkPoint);
                    neighbour.setPrev(curr);

                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour);
                    }

                }

            }

        }

        Node test = checkPoint;
        String out = "";
        while (test.prev != null) {
            out += "->" + test.getPos();
            test = test.prev;
        }

        if (test.prev == null) {
            out += "->" + test.getPos();
        }
        out = "";
        for (Node neighbour : getNeighbours(startNode, nodeMap)) {
            out += "|" + neighbour.getPos();
        }

        out = "";
        for (Node neighbour : getNeighbours(checkPoint, nodeMap)) {
            out += "|" + neighbour.getPos();
        }

        ArrayList<DIRECTION> pathAsDirections = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            Node curr = path.get(i);
            Node next = path.get(i + 1);

            if (curr.getPos().getX() == next.getPos().getX() && curr.getPos().getY() == next.getPos().getY()) {

            } else if (curr.getPos().getX() == next.getPos().getX() && curr.getPos().getY() < next.getPos().getY()) {          //Down == next node is below the curr
                pathAsDirections.add(DIRECTION.BOTTOM);
            } else if (curr.getPos().getX() == next.getPos().getX() && curr.getPos().getY() > next.getPos().getY()) {          //Up
                pathAsDirections.add(DIRECTION.TOP);
            } else if (curr.getPos().getX() > next.getPos().getX() && curr.getPos().getY() == next.getPos().getY()) {          //Left
                pathAsDirections.add(DIRECTION.LEFT);
            } else if (curr.getPos().getX() < next.getPos().getX() && curr.getPos().getY() == next.getPos().getY()) {          //Right
                pathAsDirections.add(DIRECTION.RIGHT);
            }

        }
        for (DIRECTION x :
                pathAsDirections) {
        }
        ArrayList<String> finalReg = new ArrayList<>();
        createReg(finalReg, pathAsDirections, robot.getDirection());
        for (String x :
                finalReg) {
        }
        return finalReg;
    }

    private void createReg(ArrayList<String> reg, ArrayList<DIRECTION> path, DIRECTION curr) {
        if (reg.size() == 5) {
            return;
        }

        if (path.size() == 0) {

            for (int i = reg.size(); i < 5; ++i) {
                reg.add(getRandomCard());
            }

            return;
        }
        String card = null;
        DIRECTION target = path.get(0);
        if (curr.equals(target)) {
            if (!reg.isEmpty()) {
                if (reg.get(reg.size() - 1).equals("MoveOne")) {
                    card = "MoveTwo";
                    if (cardsInHand.contains(card)) {
                        cardsInHand.add(reg.get(reg.size() - 1));
                        reg.remove(reg.size() - 1);
                        reg.add(card);
                        path.remove(0);
                        cardsInHand.remove(card);
                        createReg(reg, path, curr);
                        return;
                    }

                    card = "Again";
                    if (cardsInHand.contains(card)) {
                        reg.add(card);
                        path.remove(0);
                        cardsInHand.remove(card);
                        createReg(reg, path, curr);
                        return;
                    }
                } else if (reg.get(reg.size() - 1).equals("MoveTwo")) {
                    card = "MoveThree";
                    if (cardsInHand.contains(card)) {
                        cardsInHand.add(reg.get(reg.size() - 1));
                        reg.remove(reg.size() - 1);
                        reg.add(card);
                        path.remove(0);
                        cardsInHand.remove(card);
                        createReg(reg, path, curr);
                        return;
                    }
                }
            }
            card = "MoveOne";
            if (cardsInHand.contains(card)) {
                reg.add(card);
                path.remove(0);
                cardsInHand.remove(card);
                createReg(reg, path, curr);
                return;
            }

            card = null;
        }
        int diff = target.getAngle() - curr.getAngle();
        if (Math.abs(diff) == 180) {
            if (cardsInHand.contains("MoveBack")) {
                card = "MoveBack";
                reg.add(card);
                cardsInHand.remove(card);
                path.remove(0);
                createReg(reg, path, curr);
                return;
            } else if (cardsInHand.contains("UTurn")) {
                card = "UTurn";
                reg.add(card);
                cardsInHand.remove(card);
                createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() + 180));
                return;
            }

            if (!reg.isEmpty()) {
                if (reg.get(reg.size() - 1).equals("MoveBack") && cardsInHand.contains("Again")) {
                    card = "Again";
                    reg.add(card);
                    cardsInHand.remove(card);
                    path.remove(0);
                    createReg(reg, path, curr);
                    return;
                }
                if (reg.get(reg.size() - 1).equals("Uturn") && cardsInHand.contains("Again")) {
                    card = "Again";
                    reg.add(card);
                    cardsInHand.remove(card);
                    createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() + 180));
                    return;
                }
            }


        } else if ((diff == -90 && target == DIRECTION.RIGHT) || diff == 90) {
            if (cardsInHand.contains("TurnLeft")) {
                card = "TurnLeft";
                reg.add(card);
                cardsInHand.remove(card);
                createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() + 90));
                return;
            }
            if (!reg.isEmpty()) {
                if (reg.get(reg.size() - 1).equals("TurnLeft") && cardsInHand.contains("Again")) {
                    card = "Again";
                    reg.add(card);
                    cardsInHand.remove(card);
                    createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() + 90));
                    return;
                }
            }


        } else {
            if (cardsInHand.contains("TurnRight")) {
                card = "TurnRight";
                reg.add(card);
                cardsInHand.remove(card);
                createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() - 90));
                return;
            }
            if (!reg.isEmpty()) {
                if (reg.get(reg.size() - 1).equals("TurnRight") && cardsInHand.contains("Again")) {
                    card = "Again";
                    reg.add(card);
                    cardsInHand.remove(card);
                    createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() - 90));
                    return;
                }
            }


        }
        card = getRandomCard();
        reg.add(card);
        switch (card) {
            case "LeftTurn":
                createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() + 90));
                return;
            case "RightTurn":
                createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() - 90));
                return;
            case "UTurn":
                createReg(reg, path, DIRECTION.valueOfDirection(curr.getAngle() + 180));
                return;
            default:
                createReg(reg, path, curr);
        }
    }

    ArrayList<Node> retracePath(Node start, Node target) {
        Node curr = target;
        ArrayList<Node> path = new ArrayList<>();
        while (curr != start) {
            path.add(curr);
            curr = curr.prev;
        }

        if (curr == start) {
            path.add(curr);
        }

        ArrayList<Node> reversePath = new ArrayList<>();

        for (int i = path.size() - 1; i >= 0; --i) {
            reversePath.add(path.get(i));
        }

        return reversePath;
    }

    private boolean checkElement(String type, List<BoardElement> boardElements) {

        if (boardElements != null) {
            for (BoardElement element :
                    boardElements) {
                if (element != null) {
                    if (element.getType().equals(type)) {                                                //why is element null
                        return true;
                    }
                }

            }
        }


        return false;
    }

    int getDistanceBetweenNodes(Node A, Node B) {
        int x = Math.abs(A.getPos().getX() - B.getPos().getX());
        int y = Math.abs(A.getPos().getY() - B.getPos().getY());

        if (x > y) {
            return 14 * y + 10 * (x - y);
        } else {
            return 14 * x + 10 * (y - x);
        }

    }

    ArrayList<Node> getNeighbours(Node node, Node[][] nodeMap) {
        int X = node.getPos().getX();
        int Y = node.getPos().getY();
        ArrayList<Node> nodes = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == y || (x == 1 && y == -1) || (x == -1 && y == 1)) {
                    continue;
                }

                if (X + x >= 0 && Y + y >= 0 && Y + y < nodeMap.length && X + x < nodeMap[0].length) {
                    nodes.add(nodeMap[Y + y][X + x]);
                }


            }
        }

        return nodes;
    }

    public String getTinyPath() {
        String res = "";
        int size = 6;
        if (path.size() <= 5) {
            size = path.size();
        }

        for (int i = 1; i < path.size(); i++) {
            res += "->" + path.get(i).pos;
        }
        return res;
    }

    public void placeRobot(AIClient.Position startPos, Robot rob) {

        rob.setPosition(startPos.getX(), startPos.getY());
        if (startPos.getX() > gameMap.size() / 2) {
            rob.setDirection(DIRECTION.LEFT);
        } else {
            rob.setDirection(DIRECTION.RIGHT);
        }

        robotList.add(rob);
        gameMap.get(startPos.x).get(startPos.y).add(rob);
    }

    public void updateRobotPosition(Robot rob, int x, int y) {
        if (rob == robot) {
            for (BoardElement element :
                    gameMap.get(x).get(y)) {
                if (element instanceof CheckPoint) {
                    CheckPoint checkPoint = (CheckPoint) element;
                    if (checkPoint.getCount() == currentGoal) {
                        //currentGoal++;
                    }
                }
            }
        }

        List<BoardElement> elementList = gameMap.get(rob.getPosition().getX()).get(rob.getPosition().getY());
        Iterator<BoardElement> iter = elementList.iterator();

        while (iter.hasNext()) {
            BoardElement curr = iter.next();
            if (curr == rob) {
                iter.remove();
            }
        }

        rob.setPosition(x, y);
        gameMap.get(x).get(y).add(rob);

    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}

