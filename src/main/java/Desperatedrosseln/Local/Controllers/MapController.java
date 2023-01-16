package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Json.utils.JsonMapReader;
import Desperatedrosseln.Local.Client;
import Desperatedrosseln.Local.Protocols.SetStartingPoint;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Tiles.*;
import Desperatedrosseln.Logic.Elements.Map;
import Desperatedrosseln.Logic.Elements.MapField;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.*;

public class MapController {

    private final JsonMapReader jsonMapReader;
    private Map map;

    private int selectedRobot;

    public boolean hasStartpoint = false;

    private ArrayList<Position> unavailableStartingPoints = new ArrayList<>();

    private HashMap<StackPane, Position> startingPoints = new HashMap<>();

    private Client client;

    @FXML
    private GridPane mapGrid;
    private final int maxMapHeight;
    private List<List<List<BoardElement>>> mapAsList;
    private int tileSize = 35;


    public class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean isEqual(Position position){
            return x == position.getX() && y == position.getY();
        }

        @Override
        public String toString() {
            return "("+x+","+y+")";
        }
    }

    public MapController(GridPane mapGrid, int selectedRobot, int maxMapHeight) {
        this.mapGrid = mapGrid;
        this.jsonMapReader = new JsonMapReader();
        this.selectedRobot = selectedRobot;
        this.maxMapHeight = maxMapHeight;
        this.tileSize = maxMapHeight / 10;
        System.out.println("mapcontroller constructor: "+maxMapHeight);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @FXML
    public void showMap() {
        Platform.runLater(() -> {
            addLaserBeam(map.getMapFields());
            buildMapGrid(map.getMapFields());

            // Robot by clicking
            // ->
        });
    }

    private void addLaserBeam(List<List<MapField>> fieldList) {

        String laserOrientation;

        for (int i = 0; i < fieldList.size(); i++) {
            for (int j = 0; j < fieldList.get(i).size(); j++) {
                ArrayList<String> fieldTypes = new ArrayList<>();
                MapField adjacentField;

                for (int k = 0; k < fieldList.get(i).get(j).getTypes().size(); k++) {
                    boolean hasLaser = Objects.equals(fieldList.get(i).get(j).getTypes().get(k).getType(), "Laser");

                    if (hasLaser) {
                        Laser laser = (Laser) fieldList.get(i).get(j).getTypes().get(k);
                        laserOrientation = laser.getOrientations().get(0);

                        if (Objects.equals(laserOrientation, "top") && j < fieldList.get(i).size() - 1) {

                            for (int l = j; l < fieldList.get(i).size() - 1; l++) {
                                adjacentField = map.getMapFields().get(i).get(l+1);
                                for (int m = 0; m < adjacentField.getTypes().size(); m++) {
                                    fieldTypes.add(adjacentField.getTypes().get(m).getType());
                                }

                                createLaserBeam(laser.getOrientations(), laser.getCount(), fieldTypes, adjacentField);

                                if (fieldTypes.contains("Wall")) break;
                            }
                        } else if (Objects.equals(laserOrientation, "right") && i > 0) {
                            for (int l = i; l > 0; l--) {
                                adjacentField = map.getMapFields().get(l-1).get(j);

                                for (int m = 0; m < adjacentField.getTypes().size(); m++) {
                                    fieldTypes.add(adjacentField.getTypes().get(m).getType());
                                }
                                createLaserBeam(laser.getOrientations(), laser.getCount(), fieldTypes, adjacentField);
                                if (fieldTypes.contains("Wall")) break;
                            }

                        } else if (Objects.equals(laserOrientation, "bottom") && j > 0) {
                            for (int l = j; l > 0; l--) {
                                adjacentField = map.getMapFields().get(i).get(l-1);

                                for (int m = 0; m < adjacentField.getTypes().size(); m++) {
                                    fieldTypes.add(adjacentField.getTypes().get(m).getType());
                                }
                                createLaserBeam(laser.getOrientations(), laser.getCount(), fieldTypes, adjacentField);
                                if (fieldTypes.contains("Wall")) break;
                            }

                        } else if (Objects.equals(laserOrientation, "left") && i < fieldList.size() - 1) {
                            for (int l = i; l < map.getMapFields().size() - 1; l++) {
                                adjacentField = map.getMapFields().get(l+1).get(j);

                                for (int m = 0; m < adjacentField.getTypes().size(); m++) {
                                    fieldTypes.add(adjacentField.getTypes().get(m).getType());
                                }
                                createLaserBeam(laser.getOrientations(), laser.getCount(), fieldTypes, adjacentField);
                                if (fieldTypes.contains("Wall")) break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void createLaserBeam(ArrayList<String> orientations, int count, ArrayList<String> fieldTypes, MapField adjacentField) {
        boolean hasBlocker = fieldTypes.contains("Robot") || fieldTypes.contains("Antenna") || fieldTypes.contains("CheckPoint");

        if (!hasBlocker) {
            if (fieldTypes.contains("Wall")) {
                adjacentField.getTypes().add(new LaserBeam("LaserBeam", "Placeholder at MapController.addLaserBeam()", orientations, count, false));
            } else {
                adjacentField.getTypes().add(new LaserBeam("LaserBeam", "Placeholder at MapController.addLaserBeam()", orientations, count, true));
            }
        }
    }

    public List<List<MapField>> convertMap(List<List<List<BoardElement>>> gameMapList) {
        List<List<MapField>> mapFields = new ArrayList<>();

        for (List<List<BoardElement>> rows : gameMapList) {
            List<MapField> column = new ArrayList<>();
            for (List<BoardElement> list : rows) {
                List<BoardElement> typeList = new ArrayList<>(list);
                MapField mapField = new MapField(typeList);
                column.add(mapField);
            }
            mapFields.add(column);
        }
        return mapFields;
    }

    private void buildMapGrid(List<List<MapField>> fieldList){
        for (int i = 0; i < fieldList.size(); i++) {
            for (int j = 0; j < fieldList.get(i).size(); j++) {
                try {
                    StackPane stackPane = createGridCell(i, j, fieldList.get(i).get(j));
                    mapGrid.getChildren().add(stackPane);
                    if(fieldList.get(i).get(j).getTypes().get(0).getType().equals("StartingPoint")){
                        //ToDo
                        startingPoints.put(stackPane,new Position(i,j));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    private StackPane createGridCell(int x, int y, MapField mapField) throws IOException {
        List<String> addEmpty = Arrays.asList("Antenna", "CheckPoint", "ConveyorBelt", "RestartPoint", "StartPoint", "Energy-Space", "Wall", "Empty","Gear");
        List<BoardElement> typeList = mapField.getTypes();

        StackPane cell = new StackPane();
        cell.getStyleClass().add("tile");

        GridPane.setColumnIndex(cell, x);
        GridPane.setRowIndex(cell, y);

        Image emptyImage =
                new Image(getClass().getResource("/images/elements/empty/empty.png").toString());

        for (BoardElement boardElement : typeList) {
            ImageView stackElement = null;

            if (addEmpty.contains(boardElement.getType())) {
                ImageView empty = new ImageView(emptyImage);
                tileSize = 50;
                empty.setFitHeight(tileSize);
                empty.setPreserveRatio(true);
                cell.getChildren().add(empty);
            }

            switch (boardElement.getType()) {
                case "Pit" -> {
                    Image pitImage =
                            new Image(getClass().getResource("/images/elements/pit/pit.png").toString());
                    stackElement = new ImageView(pitImage);
                }
                case "CheckPoint" -> {
                    Image checkpointImage =
                            new Image(getClass().getResource("/images/elements/checkpoint/checkpoint.png").toString());
                    stackElement = new ImageView(checkpointImage);
                }
                case "RestartPoint" -> { stackElement = buildRestartPoint(boardElement);}
                case "StartPoint" -> {
                    Image startpointImage =
                            new Image(getClass().getResource("/images/elements/startpoint/startpoint.png").toString());
                    final boolean[] isTaken={false};
                    stackElement = new ImageView(startpointImage);
                    placeRobotOnClick(cell, stackElement, x, y, isTaken);
                }
                case "Antenna" -> stackElement = buildAntenna(boardElement);
                case "ConveyorBelt" -> stackElement = buildConveyorBelt(boardElement);
                case "Energy-Space" -> stackElement = buildEnergySpace(boardElement);
                case "Laser" -> stackElement = buildLaser(boardElement);
                case "LaserBeam" -> stackElement = buildLaserBeam(boardElement);
                case "PushPanel" -> stackElement = buildPushPanel(boardElement);
                case "Wall" -> stackElement = buildWall(boardElement);
                case "Gear" -> stackElement = buildGear(boardElement);
            }

            if (stackElement != null) {
                stackElement.setFitHeight(tileSize);
                stackElement.setPreserveRatio(true);
                cell.getChildren().add(stackElement);
            }
        }
        return cell;
    }

    @FXML
    private ImageView buildRestartPoint(BoardElement boardElement) {
        Image restartPointImage =
                new Image(getClass().getResource("/images/elements/respawnPoint/respawnPoint.png").toString());

        RestartPoint restartPoint = (RestartPoint) boardElement;
        ArrayList<String> orientations = restartPoint.getOrientations();

        ImageView stackElement = new ImageView(restartPointImage);
        System.out.println(restartPoint.getOrientations());
        return rotateElement(stackElement, orientations);

    }


    private void placeRobot(StackPane cell, int x, int y, boolean[] isTaken){
        Position currPos = new Position(x,y);
        System.out.println("pos "+currPos+" is Clicked");

        boolean isStartingPointTaken = false;

        for(Position pos: unavailableStartingPoints){
            System.out.println("99999999999999999999999");
            System.out.println(currPos+"=="+pos);
            if(pos.isEqual(currPos)){
                isStartingPointTaken = true;
                break;
            }
        }
        if(!isStartingPointTaken){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        isTaken[0] = true;


        Image robotImage;
        ImageView robot;

        if (!hasStartpoint && !isStartingPointTaken) {
            switch (selectedRobot) {
                case 1 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/brown.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 2 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/yellow.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 3 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/blue.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 4 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/green.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 5 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/orange.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 6 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/red.png").toString());
                    robot = new ImageView(robotImage);
                }
                default -> {
                    robot = new ImageView("/images/Cards/no_such_card.png");
                }
            }
            hasStartpoint = true;
            robot.setPreserveRatio(true);
            robot.setFitHeight(tileSize - 10);
            cell.getChildren().add(robot);
            //System.out.println(getRobotByXY(x, y));
           // setOnTile(getRobotByXY(x, y), 1, 1);
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<SetStartingPoint> setStartingPointJsonAdapter = moshi.adapter(SetStartingPoint.class);
            client.sendMessage("SetStartingPoint",setStartingPointJsonAdapter.toJson(new SetStartingPoint(x,y)));
        }
    }
    private void placeRobotOnClick(StackPane cell, ImageView stackElement, int x, int y, boolean[] isTaken) {
        stackElement.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                placeRobot(cell,x,y,isTaken);
        });
    }

    private void setOnTile(ImageView stackElement, int newX, int newY) {

        int oldMapIndex = mapGrid.getChildren().indexOf(stackElement.getParent());
        int newMapIndex = getMapIndex(newX, newY);

        StackPane newStackPane = (StackPane) mapGrid.getChildren().get(newMapIndex);
        StackPane oldStackPane = (StackPane) mapGrid.getChildren().get(oldMapIndex);
        newStackPane.getChildren().add(stackElement);
        oldStackPane.getChildren().remove(stackElement);
    }

    private int getMapIndex(int x, int y) {
        List<List<StackPane>> mapGridList = makeGridPane2d(mapGrid);

        int mapIndex = -1;
        int counter = 0;

        for (int i = 0; i < mapGridList.size(); i++) {
            for (int j = 0; j < mapGridList.get(i).size(); j++) {
                if (i == x && j == y) {
                    mapIndex = counter;
                    break;
                } else {
                    counter++;
                }
            }
        }
        return mapIndex;
    }

    private ImageView getRobotByXY(int x, int y) {
        int mapIndex = getMapIndex(x, y);
        StackPane stackPane = (StackPane) mapGrid.getChildren().get(mapIndex);
        ImageView imageView = (ImageView) stackPane.getChildren().get(stackPane.getChildren().size()-1);
        if (
                imageView.getImage().getUrl().equals("/images/Robots/OnTiles/red.png") ||
                        imageView.getImage().getUrl().equals("/images/Robots/OnTiles/blue.png") ||
                        imageView.getImage().getUrl().equals("/images/Robots/OnTiles/orange.png") ||
                        imageView.getImage().getUrl().equals("/images/Robots/OnTiles/green.png") ||
                        imageView.getImage().getUrl().equals("/images/Robots/OnTiles/yellow.png") ||
                        imageView.getImage().getUrl().equals("/images/Robots/OnTiles/brown.png")
        ) {
            return imageView;
        } else {
            throw new RuntimeException("There is no Robot on this Tile");
        }
    }

    private List<List<StackPane>> makeGridPane2d(GridPane mapGrid) {
        List<List<StackPane>> stackPaneList = new ArrayList<>();

        int rows = map.getMapFields().size();
        int columns = map.getMapFields().get(0).size();

        int counter = 0;

        System.out.println(mapGrid.getChildren().size());

        for (int i = 0; i < rows; i++) {
            List<StackPane> column = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                column.add((StackPane) mapGrid.getChildren().get(counter));
            }
            stackPaneList.add(column);
        }
        System.out.println(mapGrid.getChildren().get(0) == stackPaneList.get(0).get(0));
        return stackPaneList;
    }



    @FXML
    private ImageView buildAntenna(BoardElement boardElement) {
        Image antennaImage =
                new Image(getClass().getResource("/images/elements/antenna/antenna.png").toString());

        Antenna antenna = (Antenna) boardElement;
        ArrayList<String> orientations = antenna.getOrientations();

        ImageView stackElement = new ImageView(antennaImage);

        return rotateElement(stackElement, orientations);
    }

    @FXML
    private ImageView buildGear(BoardElement boardElement) {
        ImageView stackElement = null;
        Gear gear = (Gear) boardElement;
        System.out.println(gear.getOrientations());
        if (gear.getOrientations().get(0).equals("clockwise")) {
            Image gearRightImage = new Image(getClass().getResource("/images/elements/gear/gearRight.png").toString());
            stackElement = new ImageView(gearRightImage);
        } else if (gear.getOrientations().get(0).equals("counterclockwise")) {
            Image gearLeftImage = new Image(getClass().getResource("/images/elements/gear/gearLeft.png").toString());
            stackElement = new ImageView(gearLeftImage);
        }
        return stackElement;
    }

    @FXML
    private ImageView buildPushPanel(BoardElement boardElement) {
        ImageView stackElement = null;
        PushPanel pushPanel = (PushPanel) boardElement;
        //change this if to something with register
        if (pushPanel.getIsOnBoard().equals("2AA")) {
            Image pushPanel1Image =
                    new Image(getClass().getResource("/images/elements/pushPanel/pushPanel1.png").toString());
            stackElement = new ImageView(pushPanel1Image);
        } else if (pushPanel.getIsOnBoard().equals("2A")) {
            Image pushPanel2Image =
                    new Image(getClass().getResource("/images/elements/pushPanel/pushPanel2.png").toString());
            stackElement = new ImageView(pushPanel2Image);
        }
        return rotateElement(stackElement, pushPanel.getOrientations());
    }

    @FXML
    private ImageView buildEnergySpace(BoardElement boardElement) {
        ImageView stackElement = null;
        EnergySpace energySpace = (EnergySpace) boardElement;

        if (energySpace.getCount() == 1) {
            Image energySpace1Image =
                    new Image(getClass().getResource("/images/elements/energySpace/energySpace1.png").toString());
            stackElement = new ImageView(energySpace1Image);
        } else if (energySpace.getCount() == 2) {
            Image energySpace2Image =
                    new Image(getClass().getResource("/images/elements/energySpace/energySpace2.png").toString());
            stackElement = new ImageView(energySpace2Image);
        }

        return stackElement;
    }

    @FXML
    private ImageView buildWall(BoardElement boardElement) throws IOException {
        ImageView stackElement = null;
        Wall wall = (Wall) boardElement;

        if (wall.getOrientations().size() == 1) {
            Image wall1Image = new Image(getClass().getResource("/images/elements/wall/wall1.png").toString());
            stackElement = new ImageView(wall1Image);
            switch (wall.getOrientations().get(0)) {
                case "right" -> stackElement.setStyle("-fx-rotate: 90");
                case "bottom" -> stackElement.setStyle("-fx-rotate: 180");
                case "left" -> stackElement.setStyle("-fx-rotate: -90");
            }
        } else if (wall.getOrientations().size() == 2) {
            Image wall2Image =
                    new Image(getClass().getResource("/images/elements/wall/wall2.png").toString());
            stackElement = new ImageView(wall2Image);

            if (wall.getOrientations().contains("top") && wall.getOrientations().contains("right")) {
                stackElement.setStyle("-fx-rotate: 0");
            } else if (wall.getOrientations().contains("right") && wall.getOrientations().contains("bottom")) {
                stackElement.setStyle("-fx-rotate: 90");
            } else if (wall.getOrientations().contains("bottom") && wall.getOrientations().contains("left")) {
                stackElement.setStyle("-fx-rotate: 180");
            } else if (wall.getOrientations().contains("left") && wall.getOrientations().contains("top")) {
                stackElement.setStyle("-fx-rotate: -90");
            } else {
                throw new IOException("Unknown wall orientation combination");
            }
        }
        return stackElement;
    }

    @FXML
    private ImageView buildConveyorBelt(BoardElement boardElement) throws IOException {

        ConveyorBelt conveyorBelt = (ConveyorBelt) boardElement;
        ArrayList<String> orientations = conveyorBelt.getOrientations();

        ImageView stackElement = null;

        // Green
        if (conveyorBelt.getSpeed() == 1) {
            // two orientations
            Image conveyorBeltTB1Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltTB1.png").toString());
            Image conveyorBeltRB1Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltRB1.png").toString());
            Image conveyorBeltRT1Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltRT1.png").toString());

            Image conveyorBeltRTB1Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltRTB1.png").toString());
            Image conveyorBeltTBL1Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltTBL1.png").toString());
            Image conveyorBeltTRB1Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltTRB1.png").toString());
            stackElement = diffBelt(conveyorBeltTB1Image, conveyorBeltRB1Image, conveyorBeltRT1Image, conveyorBeltRTB1Image, conveyorBeltTBL1Image, conveyorBeltTRB1Image, orientations, stackElement);
            // Blue
        } else if (conveyorBelt.getSpeed() == 2) {
            Image conveyorBeltTB2Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltTB2.png").toString());
            Image conveyorBeltRB2Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltRB2.png").toString());
            Image conveyorBeltRT2Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltRT2.png").toString());

            Image conveyorBeltRTB2Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltRTB2.png").toString());
            Image conveyorBeltTBL2Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltTBL2.png").toString());
            Image conveyorBeltTRB2Image =
                    new Image(getClass().getResource("/images/elements/conveyorBelt/conveyorBeltTRB2.png").toString());
            stackElement = diffBelt(conveyorBeltTB2Image, conveyorBeltRB2Image, conveyorBeltRT2Image, conveyorBeltRTB2Image, conveyorBeltTBL2Image, conveyorBeltTRB2Image, orientations, stackElement);
        } else {
            throw new IOException("Unknown conveyorSpeed");
        }
        return stackElement;
    }

    @FXML
    private ImageView diffBelt(Image conveyorBeltTBImage, Image conveyorBeltRBImage, Image conveyorBeltRTImage, Image conveyorBeltRTBImage, Image conveyorBeltTBLImage, Image conveyorBeltTRBImage, ArrayList<String> orientations, ImageView stackElement) {
        if (orientations.size() == 2) {
            switch (orientations.get(0)) {
                case "top":
                    switch (orientations.get(1)) {
                        case "right" -> {
                            stackElement = new ImageView(conveyorBeltRBImage);
                            stackElement.setStyle("-fx-rotate: -90");
                        }
                        case "bottom" -> stackElement = new ImageView(conveyorBeltTBImage);
                        case "left" -> {
                            stackElement = new ImageView(conveyorBeltRTImage);
                            stackElement.setStyle("-fx-rotate: -90");
                        }
                    }
                    break;
                case "right":
                    switch (orientations.get(1)) {
                        case "top" -> stackElement = new ImageView(conveyorBeltRTImage);
                        case "bottom" -> stackElement = new ImageView(conveyorBeltRBImage);
                        case "left" -> {
                            stackElement = new ImageView(conveyorBeltTBImage);
                            stackElement.setStyle("-fx-rotate: 90");
                        }
                    }
                    break;
                case "bottom":
                    switch (orientations.get(1)) {
                        case "top" -> {
                            stackElement = new ImageView(conveyorBeltTBImage);
                            stackElement.setStyle("-fx-rotate: 180");
                        }
                        case "right" -> {
                            stackElement = new ImageView(conveyorBeltRTImage);
                            stackElement.setStyle("-fx-rotate: 90");
                        }
                        case "left" -> {
                            stackElement = new ImageView(conveyorBeltRBImage);
                            stackElement.setStyle("-fx-rotate: 90");
                        }
                    }
                    break;
                case "left":
                    switch (orientations.get(1)) {
                        case "top" -> {
                            stackElement = new ImageView(conveyorBeltRBImage);
                            stackElement.setStyle("-fx-rotate: 180");
                        }
                        case "right" -> {
                            stackElement = new ImageView(conveyorBeltTBImage);
                            stackElement.setStyle("-fx-rotate: -90");
                        }
                        case "bottom" -> {
                            stackElement = new ImageView(conveyorBeltRTImage);
                            stackElement.setStyle("-fx-rotate: 180");
                        }
                    }
                    break;
            }
        } else if (orientations.size() == 3) {
            List<String> orientationTail = orientations.subList(0, 3);

            switch (orientations.get(0)) {
                case "top":
                    if (orientationTail.contains("right") && orientationTail.contains("bottom")) {
                        stackElement = new ImageView(conveyorBeltTRBImage);
                    } else if (orientationTail.contains("left") && orientationTail.contains("bottom")) {
                        stackElement = new ImageView(conveyorBeltTBLImage);
                    } else if (orientationTail.contains("right") && orientationTail.contains("left")) {
                        stackElement = new ImageView(conveyorBeltRTBImage);
                        stackElement.setStyle("-fx-rotate: -90");
                    }
                    break;
                case "right":
                    if (orientationTail.contains("top") && orientationTail.contains("bottom")) {
                        stackElement = new ImageView(conveyorBeltRTBImage);
                    } else if (orientationTail.contains("bottom") && orientationTail.contains("left")) {
                        stackElement = new ImageView(conveyorBeltTRBImage);
                        stackElement.setStyle("-fx-rotate: 90");
                    } else if (orientationTail.contains("top") && orientationTail.contains("left")) {
                        stackElement = new ImageView(conveyorBeltTBLImage);
                        stackElement.setStyle("-fx-rotate: 90");
                    }
                    break;
                case "bottom":
                    if (orientationTail.contains("top") && orientationTail.contains("right")) {
                        stackElement = new ImageView(conveyorBeltTBLImage);
                        stackElement.setStyle("-fx-rotate: 180");
                    } else if (orientationTail.contains("right") && orientationTail.contains("left")) {
                        stackElement = new ImageView(conveyorBeltRTBImage);
                        stackElement.setStyle("-fx-rotate: 90");
                    } else if (orientationTail.contains("top") && orientationTail.contains("left")) {
                        stackElement = new ImageView(conveyorBeltTRBImage);
                        stackElement.setStyle("-fx-rotate: 180");
                    }
                    break;
                case "left":
                    if (orientationTail.contains("top") && orientationTail.contains("right")) {
                        stackElement = new ImageView(conveyorBeltTRBImage);
                        stackElement.setStyle("-fx-rotate: -90");
                    } else if (orientationTail.contains("right") && orientationTail.contains("bottom")) {
                        stackElement = new ImageView(conveyorBeltTBLImage);
                        stackElement.setStyle("-fx-rotate: -90");
                    } else if (orientationTail.contains("top") && orientationTail.contains("bottom")) {
                        stackElement = new ImageView(conveyorBeltRTBImage);
                        stackElement.setStyle("-fx-rotate: 180");
                    }
                    break;
            }
        }
        return stackElement;
    }

    @FXML
    private ImageView buildLaser(BoardElement boardElement) throws IOException {

        ImageView stackElement = null;
        Laser laser = (Laser) boardElement;
        if (laser.getCount() == 1) {
            Image laser1Image =
                    new Image(getClass().getResource("/images/elements/laser/laser1.png").toString());
            stackElement = new ImageView(laser1Image);
        } else if (laser.getCount() == 2) {
            Image laser2Image =
                    new Image(getClass().getResource("/images/elements/laser/laser2.png").toString());
            stackElement = new ImageView(laser2Image);
        } else if (laser.getCount() == 3) {
            Image laser3Image =
                    new Image(getClass().getResource("/images/elements/laser/laser3.png").toString());
            stackElement = new ImageView(laser3Image);
        } else {
            throw new IOException("Unknown laser count");
        }

        return rotateElement(stackElement, laser.getOrientations());
    }

    @FXML
    private ImageView buildLaserBeam(BoardElement boardElement) throws IOException {

        ImageView stackElement = null;

        LaserBeam laserBeam = (LaserBeam) boardElement;

        if (laserBeam.getCount() == 1) {
            if (laserBeam.isFullWidth()) {
                Image laserBeamCenter1Image =
                        new Image(getClass().getResource("/images/elements/laser/laserBeamCenter1.png").toString());
                stackElement = new ImageView(laserBeamCenter1Image);
            } else {
                Image laserBeamEnd1Image =
                        new Image(getClass().getResource("/images/elements/laser/laserBeamEnd1.png").toString());
                stackElement = new ImageView(laserBeamEnd1Image);
            }
        } else if (laserBeam.getCount() == 2) {
            if (laserBeam.isFullWidth()) {
                Image laserBeamCenter2Image =
                        new Image(getClass().getResource("/images/elements/laser/laserBeamCenter2.png").toString());
                stackElement = new ImageView(laserBeamCenter2Image);
            } else {
                Image laserBeamEnd2Image =
                        new Image(getClass().getResource("/images/elements/laser/laserBeamEnd2.png").toString());
                stackElement = new ImageView(laserBeamEnd2Image);
            }
        } else if (laserBeam.getCount() == 3) {
            if (laserBeam.isFullWidth()) {
                Image laserBeamCenter3Image =
                        new Image(getClass().getResource("/images/elements/laser/laserBeamCenter3.png").toString());
                stackElement = new ImageView(laserBeamCenter3Image);
            } else {
                Image laserBeamEnd3Image =
                        new Image(getClass().getResource("/images/elements/laser/laserBeamEnd3.png").toString());
                stackElement = new ImageView(laserBeamEnd3Image);
            }
        } else {
            throw new IOException("Unknown laserBeam count");
        }

        return rotateElement(stackElement, laserBeam.getOrientations());
    }

    @FXML
    private ImageView rotateElement(ImageView stackElement, ArrayList<String> orientations) {
        if (Objects.equals(orientations.get(0), "right")) {
            stackElement.setStyle("-fx-rotate: 90");
        } else if (Objects.equals(orientations.get(0), "bottom")) {
            stackElement.setStyle("-fx-rotate: 180");
        } else if (Objects.equals(orientations.get(0), "left")) {
            stackElement.setStyle("-fx-rotate: -90");
        }
        return stackElement;
    }

    public void setMap(List<List<List<BoardElement>>> gameMap) {
        map.setMapFields(convertMap(gameMap));
    }

    public void addUnavailablePosition(int x, int y ){
        unavailableStartingPoints.add(new Position(x,y));
    }
    public void addEnemiesToTheScreen(int x, int y ,int robotID){
        int mapIndex = getMapIndex(x, y);
        System.out.println("********************"+mapIndex);
        placeEnemyRobots((StackPane) mapGrid.getChildren().get(mapIndex),x, y,robotID);
    }

    void placeEnemyRobots(StackPane cell, int x, int y, int robotID){
        Position currPos = new Position(x,y);

        boolean isStartingPointTaken = false;



        Image robotImage;
        ImageView robot;

            switch (robotID) {
                case 1 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/brown.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 2 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/yellow.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 3 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/blue.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 4 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/green.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 5 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/orange.png").toString());
                    robot = new ImageView(robotImage);
                }
                case 6 -> {
                    robotImage = new Image(getClass().getResource("/images/Robots/OnTiles/red.png").toString());
                    robot = new ImageView(robotImage);
                }
                default -> {
                    robot = new ImageView("/images/Cards/no_such_card.png");
                }
            }

            robot.setPreserveRatio(true);
            robot.setFitHeight(tileSize - 10);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cell.getChildren().add(robot);
            }
        });

        


    }

    public void setClient(Client client) {
        this.client =client;
    }

    public void autoSelectStartPoint() {
        Position finalPos = null;
        for (int i = 0; i < mapAsList.size(); i++) {
            for (int j = 0; j < mapAsList.get(i).size(); j++) {
                for (BoardElement element :
                        mapAsList.get(i).get(j)) {
                    if (element.getType().equals("StartPoint") && !hasStartpoint) {

                        boolean isPosTaken = false;

                        Position currPos = new Position(i, j);
                        if (!unavailableStartingPoints.isEmpty()) {
                            for (Position pos : unavailableStartingPoints) {
                                if (pos.isEqual(currPos)) {
                                    isPosTaken = true;
                                }
                            }
                        }
                        if (!isPosTaken) {
                            finalPos = new Position(currPos.x, currPos.y);
                            hasStartpoint = true;

                        }


                    }
                }
            }
        }
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SetStartingPoint> setStartingPointJsonAdapter = moshi.adapter(SetStartingPoint.class);
        client.sendMessage("SetStartingPoint",setStartingPointJsonAdapter.toJson(new SetStartingPoint(finalPos.x, finalPos.y)));
        int mapIndex = getMapIndex(finalPos.x, finalPos.y);
        boolean[] isTaken = {false};
        placeRobot((StackPane) mapGrid.getChildren().get(mapIndex),finalPos.x, finalPos.y,isTaken);

    }

    public void runAutoStartPointSelection(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                autoSelectStartPoint();
            }
        });
    }
    public void setMapAsList(List<List<List<BoardElement>>> mapAsList) {
        this.mapAsList = mapAsList;
    }





}