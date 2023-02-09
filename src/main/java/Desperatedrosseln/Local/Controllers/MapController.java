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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MapController {

    private final JsonMapReader jsonMapReader;
    private Map map;

    private final int selectedRobot;

    public boolean hasStartpoint = false;

    private ArrayList<Position> unavailableStartingPoints = new ArrayList<>();

    private HashMap<StackPane, Position> startingPoints = new HashMap<>();

    private Client client;

    private static List<ImageView> robotImages = new ArrayList<>();

    @FXML
    private GridPane mapGrid;
    private List<List<List<BoardElement>>> mapAsList;
    private int tileSize = 35;



    private HashMap<Integer, Position> idToPosition = new HashMap<>();

    private static final Logger logger = LogManager.getLogger();

    @FXML private Button upgradeButton;

    @FXML private Button noUpgradeButton;


    public class Position {
        private int x;
        private int y;

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

        public void setPosition(int x, int y) {
            this.setX(x);
            this.setY(y);
        }

        @Override
        public String toString() {
            return "("+x+","+y+")";
        }
    }

    public HashMap<Integer, Position> getIdToPosition() {
        return idToPosition;
    }

    public MapController(GridPane mapGrid, int selectedRobot) {
        this.mapGrid = mapGrid;
        this.jsonMapReader = new JsonMapReader();
        this.selectedRobot = selectedRobot;
    }

    public void setUpgradeButtons(Button upgradeButton, Button noUpgradeButton) {
        this.upgradeButton = upgradeButton;
        this.noUpgradeButton = noUpgradeButton;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public List<ImageView> getRobotImages() {
        return robotImages;
    }

    @FXML
    public void showMap() {
        addLaserBeam(map.getMapFields());
        buildMapGrid(map.getMapFields());
        glow();
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
        //CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> {
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
            });
        //}).thenRun(() -> {

        //});
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
                case "RestartPoint" -> { stackElement = buildRestartPoint(boardElement);}
                case "StartPoint" -> {
                    Image startpointImage =
                            new Image(getClass().getResource("/images/elements/startpoint/startpoint.png").toString());
                    final boolean[] isTaken={false};
                    stackElement = new ImageView(startpointImage);
                    handleStartingPoint(stackElement, x, y, isTaken);
                }
                case "CheckPoint" ->stackElement = buildCheckpoint(boardElement);
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

    public void setTileSize(int computedTileSize) {
        tileSize = computedTileSize;
    }




    @FXML
    private ImageView buildCheckpoint(BoardElement boardElement) throws IOException {

        CheckPoint checkPoint = (CheckPoint) boardElement;
        //ArrayList<String> orientations = checkPoint.getOrientations();

        ImageView element = null;

        // Green
        if (checkPoint.getCount() == 1) {
            // two orientations
            Image checkPoint1 =
                    new Image(getClass().getResource("/images/elements/checkpoint/checkpoint1.png").toString());
            element = new ImageView(checkPoint1);
            // Blue
        } else if (checkPoint.getCount() == 2) {
            Image checkPoint2 =
                    new Image(getClass().getResource("/images/elements/checkpoint/checkpoint2.png").toString());
            element = new ImageView(checkPoint2);

        } else if (checkPoint.getCount() == 3) {
            Image checkPoint3 =
                    new Image(getClass().getResource("/images/elements/checkpoint/checkpoint3.png").toString());
            element = new ImageView(checkPoint3);

        }else if (checkPoint.getCount() == 4) {
            Image checkPoint4 =
                    new Image(getClass().getResource("/images/elements/checkpoint/checkpoint4.png").toString());
            element = new ImageView(checkPoint4);

        }else if (checkPoint.getCount() == 5) {
            Image checkPoint5 =
                    new Image(getClass().getResource("/images/elements/checkpoint/checkpoint5.png").toString());
            element = new ImageView(checkPoint5);
        }else{
            throw new IOException("Unknown");
        }
        return element;
    }
    @FXML
    private ImageView buildRestartPoint(BoardElement boardElement) {
        Image restartPointImage =
                new Image(getClass().getResource("/images/elements/respawnPoint/respawnPoint.png").toString());

        RestartPoint restartPoint = (RestartPoint) boardElement;
        ArrayList<String> orientations = restartPoint.getOrientations();

        ImageView stackElement = new ImageView(restartPointImage);
        return rotateElement(stackElement, orientations);
    }


    private void requestStartingPoint(int x, int y, boolean[] isTaken) {
        Position position = new Position(x,y);

        boolean isStartingPointTaken = false;

        for(Position pos: unavailableStartingPoints){
            if(pos.isEqual(position)){
                isStartingPointTaken = true;
                break;
            }
        }
        //if(!isStartingPointTaken){}

        isTaken[0] = true;

        if (!hasStartpoint && !isStartingPointTaken) {
            hasStartpoint = true;
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<SetStartingPoint> setStartingPointJsonAdapter = moshi.adapter(SetStartingPoint.class);
            client.sendMessage("SetStartingPoint",setStartingPointJsonAdapter.toJson(new SetStartingPoint(x,y)));
            upgradeButton.setDisable(false);
            noUpgradeButton.setDisable(false);
        }
    }
    private void handleStartingPoint(ImageView stackElement, int x, int y, boolean[] isTaken) {
        stackElement.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            requestStartingPoint(x,y,isTaken);
        });
    }

    public ImageView initRobot(int robotId, int x, int y) throws IOException {
        ImageView robot = diffRobotImage(robotId);
        robot.setPreserveRatio(true);
        robot.setFitHeight(tileSize - 10);

        StackPane cell = (StackPane) mapGrid.getChildren().get(getMapIndex(x, y));
        cell.getChildren().add(robot);
        robotImages.add(robot);
        idToPosition.put(robotId, new Position(x, y));

        if(x > 4){
            rotateRobot(robotId, "clockwise");
            rotateRobot(robotId, "clockwise");
        }

        logger.debug(idToPosition.get(robotId).toString());
        return robot;
    }

    //private void moveRobot(robotId)

    public void move(int robotId, int newX, int newY) {
        Platform.runLater(() -> {
            Position position = idToPosition.get(robotId);

            int oldX = position.getX();
            int oldY = position.getY();

            int newMapIndex = getMapIndex(newX, newY);

            ImageView robotImage = getRobotFromTile(oldX, oldY);

            // Removes the images from the old Tile and changes the robos position to the new one
            removeRobot(oldX, oldY);
            idToPosition.get(robotId).setPosition(newX, newY);

            // adds the new Image to the new Tile
            StackPane newStackPane = (StackPane) mapGrid.getChildren().get(newMapIndex);
            newStackPane.getChildren().add(robotImage);
        });
    }

    public void rotateRobot(int id, String direction) {
        ImageView robotImage = getRobotById(id);
        logger.info(robotImage + " " + id);
        List<String> orientations = new ArrayList<>();

        if (direction.equals("clockwise")) {
            orientations.add("right");
        } else if (direction.equals("counterclockwise")) {
            orientations.add("left");
        } else {
            logger.warn("Unknown PlayerTurning direction");
        }
        rotateElement(robotImage, orientations);
    }

    public void removeRobot(int x, int y) {
        Platform.runLater(() -> {
            int mapIndex = getMapIndex(x, y);
            StackPane cell = (StackPane) mapGrid.getChildren().get(mapIndex);
            logger.info("cell size: " + cell.getChildren().size());
            logger.info(robotImages);
            for (Node node: cell.getChildren()){
                ImageView imageView = (ImageView) node;
                if (robotImages.contains(imageView)) {
                    cell.getChildren().remove(imageView);
                }
            }
        });
    }

    public void removeRobotById(int id) {
        int x = idToPosition.get(id).getX();
        int y = idToPosition.get(id).getX();
        removeRobot(x, y);
    }

    public void addRobot(int id) {
        int x = idToPosition.get(id).getX();
        int y = idToPosition.get(id).getX();
        removeRobot(x, y);
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

    private ImageView getRobotById(int robotId) {
        Position position = idToPosition.get(robotId);
        int x = position.getX();
        int y = position.getY();
        return getRobotFromTile(x, y);
    }


    private ImageView getRobotFromTile(int x, int y) {
        int mapIndex = getMapIndex(x, y);

        StackPane stackPane = (StackPane) mapGrid.getChildren().get(mapIndex);

        for (Node imageNode : stackPane.getChildren()) {
            ImageView imageView = (ImageView) imageNode;
            if (robotImages.contains(imageView)) {
                return imageView;
            }
        }
        return null;
    }



    private List<List<StackPane>> makeGridPane2d(GridPane mapGrid) {
        List<List<StackPane>> stackPaneList = new ArrayList<>();

        int rows = map.getMapFields().size();
        int columns = map.getMapFields().get(0).size();
        int counter = 0;

        for (int i = 0; i < rows; i++) {
            List<StackPane> column = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                column.add((StackPane) mapGrid.getChildren().get(counter));
            }
            stackPaneList.add(column);
        }
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
        if (pushPanel.getRegisters().contains(1)) {
            Image pushPanel1Image =
                    new Image(getClass().getResource("/images/elements/pushPanel/pushPanel1.png").toString());
            stackElement = new ImageView(pushPanel1Image);
        } else if (pushPanel.getRegisters().contains(2)) {
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
    private synchronized ImageView
    rotateElement(ImageView stackElement, List<String> orientations) {
        if (Objects.equals(orientations.get(0), "right")) {
            stackElement.setRotate(stackElement.getRotate() + 90);
        } else if (Objects.equals(orientations.get(0), "bottom")) {
            stackElement.setRotate(stackElement.getRotate() + 180);
        } else if (Objects.equals(orientations.get(0), "left")) {
            stackElement.setRotate(stackElement.getRotate() - 90);
        }
        return stackElement;
    }

    public void setMap(List<List<List<BoardElement>>> gameMap) {
        map.setMapFields(convertMap(gameMap));
    }

    public void addUnavailablePosition(int x, int y ){
        unavailableStartingPoints.add(new Position(x,y));
    }
    public void addRobotToUI(int robotId, int x, int y){
        CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> {
                try {
                    initRobot(robotId, x, y);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }).thenRun(() -> {

        });
    }

    private ImageView diffRobotImage(int robotID) {
        Image robotImage;
        ImageView robot;
        switch (robotID) {
            case 1 -> {
                robotImage = new Image(getClass().getResource("/images/robots/ontiles/brownOnTiles.png").toString());
                robot = new ImageView(robotImage);
            }
            case 2 -> {
                robotImage = new Image(getClass().getResource("/images/robots/ontiles/yellowOnTiles.png").toString());
                robot = new ImageView(robotImage);
            }
            case 3 -> {
                robotImage = new Image(getClass().getResource("/images/robots/ontiles/blueOnTiles.png").toString());
                robot = new ImageView(robotImage);
            }
            case 4 -> {
                robotImage = new Image(getClass().getResource("/images/robots/ontiles/greenOnTiles.png").toString());
                robot = new ImageView(robotImage);
            }
            case 5 -> {
                robotImage = new Image(getClass().getResource("/images/robots/ontiles/orangeOnTiles.png").toString());
                robot = new ImageView(robotImage);
            }
            case 6 -> {
                robotImage = new Image(getClass().getResource("/images/robots/ontiles/redOnTiles.png").toString());
                robot = new ImageView(robotImage);
            }
            default -> {
                robot = new ImageView("/images/Cards/no_such_card.png");
            }
        }
        return robot;
    }

    public Client getClient() {
        return client;
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
        requestStartingPoint(finalPos.x, finalPos.y,isTaken);
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

    @FXML
    public void glow(){
        addBgGlow();
    }

    @FXML
    private void addBgGlow() {
        DropShadow pinkGlow = new DropShadow();
        pinkGlow.setOffsetY(0f);
        pinkGlow.setOffsetX(0f);
        pinkGlow.setColor(Color.rgb(246, 1, 157));
        pinkGlow.setWidth(60);
        pinkGlow.setHeight(60);
        mapGrid.setEffect(pinkGlow);
    }
}