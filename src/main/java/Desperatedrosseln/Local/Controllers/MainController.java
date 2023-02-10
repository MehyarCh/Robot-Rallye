package Desperatedrosseln.Local.Controllers;

//import Desperatedrosseln.Local.Client;

import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Local.CardLabels.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
import Desperatedrosseln.Logic.Cards.Upgrade.SpamBlocker;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.net.URI;
import java.awt.Desktop;
import java.util.concurrent.atomic.AtomicBoolean;

import static Desperatedrosseln.Local.Controllers.LoginController.client;

public class MainController {

    DataOutputStream dos;
    DataInputStream dis;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private MapController mapController;

    private List<String> handValues = new ArrayList<>();
    private List<String> registerValues = new ArrayList<>();

    private List<String> upgradeValues = new ArrayList<>();
    private List<String> tempUpgradeValues = new ArrayList<>(
            Arrays.asList("null", "null", "null")
    );
    private List<String> permUpgradeValues = new ArrayList<>(
            Arrays.asList("null", "null", "null")
    );



    private String selectedUpgrade = "null";

    @FXML
    private GridPane mapGrid;
    @FXML
    private GridPane cardWrapper;

    @FXML
    private StackPane centerStack;
    @FXML
    private Button send_button;
    @FXML
    private TextField chat_input;
    @FXML
    private VBox messageBoard;
    @FXML
    private TextFlow chatlog;
    @FXML
    private StackPane registerCardOne;
    @FXML
    private StackPane registerCardTwo;
    @FXML
    private StackPane registerCardThree;
    @FXML
    private StackPane registerCardFour;
    @FXML
    private StackPane registerCardFive;
    @FXML
    private StackPane handCardOne;
    @FXML
    private StackPane handCardTwo;
    @FXML
    private StackPane handCardThree;
    @FXML
    private StackPane handCardFour;
    @FXML
    private StackPane handCardFive;
    @FXML
    private StackPane handCardSix;
    @FXML
    private StackPane handCardSeven;
    @FXML
    private StackPane handCardEight;
    @FXML
    private StackPane handCardNine;
    @FXML
    private Button programdone;

    @FXML
    public Button upgradeButton;

    @FXML
    public Button noUpgradeButton;

    @FXML
    private StackPane upgradeCard1;

    @FXML
    private StackPane upgradeCard2;

    @FXML
    private StackPane upgradeCard3;

    @FXML
    private StackPane upgradeCard4;

    @FXML
    private StackPane upgradeCard5;

    @FXML
    private StackPane upgradeCard6;

    @FXML private StackPane tempUpgradeCard1;
    @FXML private StackPane tempUpgradeCard2;
    @FXML private StackPane tempUpgradeCard3;
    @FXML private StackPane permUpgradeCard1;
    @FXML private StackPane permUpgradeCard2;
    @FXML private StackPane permUpgradeCard3;

    @FXML private Label mainRobot1;
    @FXML private Label mainRobot2;
    @FXML private Label mainRobot3;
    @FXML private Label mainRobot4;
    @FXML private Label mainRobot5;
    @FXML private Label mainRobot6;

    private List<StackPane> registerCards;
    private List<StackPane> handCards;
    private List<StackPane> upgradeCards;
    private List<StackPane> tempUpgradeCards;
    private List<StackPane> permUpgradeCards;

    private final List<String> permanentUpgradeTypes = new ArrayList<>(
            Arrays.asList(
                    "AdminPrivilege",
                    "RearLaser"
            )
    );

    private final List<String> temporaryUpgradeTypes = new ArrayList<>(
            Arrays.asList(
                    "MemorySwap",
                    "SpamBlocker"
            )
    );

    private final Map<String, String> upgradeToDescription = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, String>(
                    "AdminPrivilege",
                    "Once per round, you may give your robot priority for one register."
            ),
            new AbstractMap.SimpleEntry<String, String>(
                    "RearLaser",
                    "Your robot may shoot backward as well as forward."
            ),
            new AbstractMap.SimpleEntry<String, String>(
                    "MemorySwap",
                    "Draw three cards. Then choose three from your hand to put on top of your deck."
            ),
            new AbstractMap.SimpleEntry<String, String>(
                    "SpamBlocker",
                    "Replace each SPAM damage card in your hand with a card from the top of your deck."
            )
    );

    private final Map<String, Integer> upgradeToEnergy = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Integer>(
                    "AdminPrivilege",
                    3
            ),
            new AbstractMap.SimpleEntry<String, Integer>(
                    "RearLaser",
                    2
            ),
            new AbstractMap.SimpleEntry<String, Integer>(
                    "MemorySwap",
                    1
            ),
            new AbstractMap.SimpleEntry<String, Integer>(
                    "SpamBlocker",
                    3
            )
    );

    private int selectedRobot = 0;

    private MoveOneLabel moveOneLabel;
    private MoveOneLabel anotherMoveOneLabel;
    private MoveTwoLabel moveTwoLabel;

    private MoveThreeLabel moveThreeLabel;

    private MoveBack moveBackLabel;

    private Again againLabel;

    private PowerUp powerUpLabel;

    private TurnLeft turnLeftLabel;

    private TurnRight turnRightLabel;

    private UTurn uTurnLabel;
    public boolean isProgrammingDone = false;
    Moshi moshi = new Moshi.Builder().build();
    @FXML
    private Label profileIcon;
    @FXML
    private Label playerName;
    @FXML
    private Label timeLabel;
    @FXML
    private Label energyLabel;
    @FXML
    private Label textBrown;
    @FXML
    private Label textYellow;
    @FXML
    private Label textBlue;
    @FXML
    private Label textGreen;
    @FXML
    private Label textOrange;
    @FXML
    private Label textRed;

    @FXML
    private HBox upgradeBar;

    @FXML
    private GridPane sidebar;

    @FXML private GridPane navbar;

    @FXML
    private Label phaseLabel;
    @FXML
    private Label instructionLabel;
    @FXML
    private Label shopLabel;
    private static final Logger logger = LogManager.getLogger(MainController.class);

    public Timer timer;

    private int seconds = 30;



    public MainController() {

        setStreams();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainScene.fxml"));
        loader.setController(this);

        try {
            root = loader.load();
            scene = new Scene(root);

            String mainCss = this.getClass().getResource("/css/main.css").toExternalForm();
            scene.getStylesheets().add(mainCss);

            String modulesCss = this.getClass().getResource("/css/modules.css").toExternalForm();
            scene.getStylesheets().add(modulesCss);


            String stateCss = this.getClass().getResource("/css/state.css").toExternalForm();
            scene.getStylesheets().add(stateCss);

            playerName.setText(client.getName());
            phaseLabel.setText("Setup Phase");
            instructionLabel.setText("Choose your Starting Point!");


            //TESTING THE MOVE CARDS FUNCTION

            //List of the StackPanes which represent the register

            registerCards = new ArrayList<>(
                    Arrays.asList(
                            registerCardOne,
                            registerCardTwo,
                            registerCardThree,
                            registerCardFour,
                            registerCardFive)
            );

            handCards = new ArrayList<>(
                    Arrays.asList(
                            handCardOne,
                            handCardTwo,
                            handCardThree,
                            handCardFour,
                            handCardFive,
                            handCardSix,
                            handCardSeven,
                            handCardEight,
                            handCardNine
                    )
            );

            upgradeCards = new ArrayList<>(
                    Arrays.asList(
                            upgradeCard1,
                            upgradeCard2,
                            upgradeCard3,
                            upgradeCard4,
                            upgradeCard5,
                            upgradeCard6
                    )
            );

            tempUpgradeCards = new ArrayList<>(
                    Arrays.asList(
                        tempUpgradeCard1,
                        tempUpgradeCard2,
                        tempUpgradeCard3
                    )
            );

            permUpgradeCards = new ArrayList<>(
                    Arrays.asList(
                        permUpgradeCard1,
                        permUpgradeCard2,
                        permUpgradeCard3
                    )
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MapController getMapController() {
        return mapController;
    }

    public void startMainScene(Stage stage, int selectedRobot) throws IOException {
        client.isMainSceneStarted = true;
        this.stage = stage;
        mapController = new MapController(mapGrid, selectedRobot);
        mapController.setClient(client);
        setProfileIcon();
        handleUpgradeClick();
        handleUpgradeHover();
        programdone.setDisable(true);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            changeTileSize();
        };
        stage.heightProperty().addListener(stageSizeListener);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setScene(scene);
                stage.setMaximized(false);
                stage.setMaximized(true);
                mapController.setTileSize(calcMaxMapHeight() / 12);
                glow();
                mapController.setUpgradeButtons(upgradeButton, noUpgradeButton);
                upgradeButton.setDisable(true);
                noUpgradeButton.setDisable(true);
                showPlayingRobotsLabels();
                }
        });

        stage.setOnCloseRequest(event -> {
            client.logOut();
            logger.info("client closed his lobby screen");
        });
    }

    private void changeTileSize() {
        int maxSize = calcMaxMapHeight();
        int tileSize = maxSize / 15;

        for (Node tile : mapGrid.getChildren()) {
            StackPane stackPane = (StackPane) tile;
            for (Node boardElement : stackPane.getChildren()) {
                ImageView imageView = (ImageView) boardElement;
                imageView.setFitWidth(tileSize);
            }
        }
    }





    public void setPhaseLabel(String label){
        Platform.runLater(() -> {
            phaseLabel.setText(label);
        });
    }
    public void setInstructionLabel(String label){
        Platform.runLater(() -> {
            instructionLabel.setText(label);
        });
    }
    @FXML
    private void setProfileIcon() {
        switch (selectedRobot) {
            case 1 -> profileIcon.setId("player-icon--1");
            case 2 -> profileIcon.setId("player-icon--2");
            case 3 -> profileIcon.setId("player-icon--3");
            case 4 -> profileIcon.setId("player-icon--4");
            case 5 -> profileIcon.setId("player-icon--5");
            case 6 -> profileIcon.setId("player-icon--6");
        }
    }

    @FXML
    private void showPlayingRobotsLabels(){
        if (client.getRobotIDs().contains(1)){
            mainRobot1.setOpacity(1.0);
            textBrown.setTextFill(Color.rgb(255, 255, 255, 1.0));
        }
        if (client.getRobotIDs().contains(2)){
            mainRobot2.setOpacity(1.0);
            textYellow.setTextFill(Color.rgb(255, 255, 255, 1.0));
        }
        if (client.getRobotIDs().contains(3)){
            mainRobot3.setOpacity(1.0);
            textBlue.setTextFill(Color.rgb(255, 255, 255, 1.0));
        }
        if (client.getRobotIDs().contains(4)){
            mainRobot4.setOpacity(1.0);
            textGreen.setTextFill(Color.rgb(255, 255, 255, 1.0));
        }
        if (client.getRobotIDs().contains(5)){
            mainRobot5.setOpacity(1.0);
            textOrange.setTextFill(Color.rgb(255, 255, 255, 1.0));
        }
        if (client.getRobotIDs().contains(6)){
            mainRobot6.setOpacity(1.0);
            textRed.setTextFill(Color.rgb(255, 255, 255, 1.0));
        }
    }

    @FXML
    public void showOverlay(String content) {
        VBox overlay = new VBox();
        Label label = new Label(content);
        Button button = new Button("RETURN TO LOBBY");
        button.getStyleClass().addAll(
                Arrays.asList(
                        "button",
                        "button--sm",
                        "button--secondary"
                )
        );
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                LobbyController lobbyController = new LobbyController();
                lobbyController.startLobbyScene(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        label.getStyleClass().add("main-overlay-label");
        overlay.getStyleClass().add("main-overlay");
        overlay.setFillWidth(true);
        overlay.getChildren().add(label);
        addGlow(label, 0.9);
        overlay.getChildren().add(button);
        centerStack.getChildren().add(overlay);
    }

    @FXML public void showRestartOverlay() {

        VBox overlay = new VBox();
        overlay.getStyleClass().add("main-overlay");
        Label label = new Label("Choose the direction your robot should look at after the restart");
        label.getStyleClass().add("main-overlay-label");
        label.setStyle("-fx-font-size: 24px");
        overlay.getChildren().add(label);

        Button up = new Button("RETURN TO LOBBY");
        up.getStyleClass().add("button");
        up.getStyleClass().add("send-button");
        up.setRotate(-90);
        GridPane.setColumnIndex(up, 1);
        GridPane.setRowIndex(up, 0);

        Button right = new Button("RETURN TO LOBBY");
        right.getStyleClass().add("button");
        right.getStyleClass().add("send-button");
        right.setRotate(0);
        GridPane.setColumnIndex(right, 2);
        GridPane.setRowIndex(right, 1);

        Button down = new Button("RETURN TO LOBBY");
        down.getStyleClass().add("button");
        down.getStyleClass().add("send-button");
        down.setRotate(90);
        GridPane.setColumnIndex(down, 1);
        GridPane.setRowIndex(down, 2);

        Button left = new Button("RETURN TO LOBBY");
        left.getStyleClass().add("button");
        left.getStyleClass().add("send-button");
        left.setRotate(180);
        GridPane.setColumnIndex(left, 0);
        GridPane.setRowIndex(left, 1);

        GridPane grid = new GridPane();
        grid.getChildren().addAll(
                Arrays.asList(
                        up,
                        left,
                        down,
                        right
                )
        );
        grid.setStyle("-fx-alignment: center");

        overlay.getChildren().add(grid);

        Platform.runLater(() -> centerStack.getChildren().add(overlay));


        JsonAdapter<RebootDirection> rebootDirectionJsonAdapter = moshi.adapter(RebootDirection.class);

        up.setOnMouseClicked(t -> {
            client.sendMessage("RebootDirection", rebootDirectionJsonAdapter.toJson(new RebootDirection("top")));
            hideOverlay();
        });
        right.setOnMouseClicked(t -> {
            client.sendMessage("RebootDirection", rebootDirectionJsonAdapter.toJson(new RebootDirection("right")));
            hideOverlay();
        });
        down.setOnMouseClicked(t -> {
            client.sendMessage("RebootDirection", rebootDirectionJsonAdapter.toJson(new RebootDirection("bottom")));
            hideOverlay();
        });
        left.setOnMouseClicked(t -> {
            client.sendMessage("RebootDirection", rebootDirectionJsonAdapter.toJson(new RebootDirection("left")));
            hideOverlay();
        });
    }

    @FXML
    private void hideOverlay() {
        centerStack.getChildren().remove(centerStack.getChildren().size() - 1);
    }


    public void startTimer() {
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                            timeLabel.setText(String.valueOf(seconds--));
                            if (seconds < 0) {
                                if (!isProgrammingDone) {
                                    addChatMessage("Info" + ":" + "Timer ended!");
                                    sendRandomCards();
                                    client.sendChatMessage("got sent random cards", -1);
                                }
                                timer.cancel();
                            }
                        }
                );
            }
        }, 0, 1000);
    }

    public void resetTimer() {
        Platform.runLater(()-> {
            timer.cancel();
            seconds = 30;
            timeLabel.setText(String.valueOf(seconds));
        });
    }

    public void fillHand() {
        handValues.addAll(client.getCardsInHand());
    }

    public void fillDummyHand() {
        handValues.add("RightTurn");
        handValues.add("NoImage");
        handValues.add("Move1");
        handValues.add("LeftTurn");
        handValues.add("U-Turn");
        handValues.add("NoImage");
        handValues.add("NoImage");
        handValues.add("Move3");
        handValues.add("Move3");
    }

    public void initRegisterValues() {
        for (int i = 0; i < 5; i++) {
            registerValues.add(null);
        }
    }

    public void updateCardImages() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (String cardValue : handValues) {
                    ImageView stackElement = new ImageView(showCardImage(cardValue));
                    int parentWidth = (int) handCards.get(i).getWidth();
                    stackElement.setPreserveRatio(true);
                    stackElement.setFitWidth(parentWidth);
                    handCards.get(i++).getChildren().add(stackElement);
                }
            }
        });

    }

    public Image showCardImage(String cardValue) {
        return switch (cardValue) {
            case "MoveBack" -> new Image(getClass().getResource("/images/card/moveback.jpg").toString());
            case "MoveOne" -> new Image(getClass().getResource("/images/card/move1.jpg").toString());
            case "MoveTwo" -> new Image(getClass().getResource("/images/card/move2.jpg").toString());
            case "TurnLeft" -> new Image(getClass().getResource("/images/card/leftTurn.jpg").toString());
            case "MoveThree" -> new Image(getClass().getResource("/images/card/move3.jpg").toString());
            case "TurnRight" -> new Image(getClass().getResource("/images/card/rightTurn.jpg").toString());
            case "UTurn" -> new Image(getClass().getResource("/images/card/u-turn.jpg").toString());
            case "Again" -> new Image(getClass().getResource("/images/card/again.jpg").toString());
            case "PowerUp" -> new Image(getClass().getResource("/images/card/powerup.jpg").toString());
            case "Spam" -> new Image(getClass().getResource("/images/card/spam.jpg").toString()); //ToDo: change picture
            case "Virus" -> new Image(getClass().getResource("/images/card/virus.jpg").toString());
            case "Trojan" -> new Image(getClass().getResource("/images/card/trojan.jpg").toString());
            case "Worm" -> new Image(getClass().getResource("/images/card/worm.jpg").toString());
            default -> new Image(getClass().getResource("/images/card/no_such_card.png").toString());
        };
    }

    @FXML
    public void onMessageSend(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            onClickSend();
        }
    }

    public void onProgrammingDone() {
        int ctr = 0;

        for (int i=0; i<5;++i){
            if(registerValues.get(i) != null){
                ++ctr;
            }
        }

        if (ctr == 5) {
            //sendCards();
            isProgrammingDone = true;
            logger.info(client.getName() + " is done programming");
        }
    }

    @FXML
    public void onPlayCard() {
        if (client.getIsMyTurn()) {
            client.playCard();
            programdone.setDisable(true);
        } else {
            addChatMessage("ERROR" + ":" + "not your Turn.");
        }
    }

    @FXML
    void onClickSend() {

        if (!chat_input.getText().isEmpty()) {
            String msg = chat_input.getText();

            client.sendChatMessage(msg, -1);

            chat_input.setText("");
            chat_input.requestFocus();
        }

    }

    @FXML
    public void addChatMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                logger.info("Message: " + message);

                String myName = client.getName();
                logger.info(myName);

                try{
                String messageName = message.split(":")[0].trim();
                String messageContent = message.split(":")[1].trim();
                logger.info(messageName);

                VBox messageWrapper = new VBox();
                messageWrapper.getStyleClass().add("message-wrapper");

                Label messageLabel = new Label(messageName);
                messageLabel.getStyleClass().add("message-label");
                Label messageText = new Label(messageContent);
                messageText.getStyleClass().add("message-text");
                HBox messageBox = new HBox(messageText);
                //addGlow(messageBox, 0.8);
                messageBox.getStyleClass().add("message");
                if (Objects.equals(myName, messageName)) {
                    messageBox.getStyleClass().add("message--secondary");
                } else if (Objects.equals("Info", messageName) || Objects.equals("ERROR", messageName)) {
                    messageBox.getStyleClass().add("message--server");
                }
                else {
                    messageBox.getStyleClass().add("message--primary");
                }
                messageWrapper.getChildren().add(messageLabel);
                messageWrapper.getChildren().add(messageBox);
                messageBoard.getChildren().add(messageWrapper);}
                catch(ArrayIndexOutOfBoundsException e){
                    logger.warn("You already chose this robot!");
                }

            }
        });
    }

    private void setStreams() {
        this.dos = client.getOutputStr();
        this.dis = client.getInputStr();
    }

    public int getSelectedRobot() {
        return selectedRobot;
    }

    public void setSelectedRobot(int selectedRobot) {
        this.selectedRobot = selectedRobot;
    }

    public void sendRandomCards() {
        logger.debug(client + " got sent the following random cards: " + handValues);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);
                for (int firstFreeRegister = 0; firstFreeRegister < 5; firstFreeRegister++) {
                    if (registerValues.get(firstFreeRegister) == null) {
                        int index = (int) (Math.random() * handValues.size());
                        if (index == handValues.size()) {
                            --index;
                        }
                        if (handValues.get(index) != null) {

                            // adding to register
                            registerValues.set(firstFreeRegister, handValues.get(index));
                            client.sendMessage("SelectedCard", selectedCardJsonAdapter.toJson(new SelectedCard(handValues.get(index), firstFreeRegister)));
                            // adding register image
                            Image image = showCardImage(handValues.get(index));
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(110);
                            imageView.setPreserveRatio(true);
                            registerCards.get(firstFreeRegister).getChildren().add(imageView);
                            // Removing from Hand
                            handValues.set(index, null);
                            // Removing handImage
                            handCards.get(index).getChildren().remove(0);
                        }
                        --firstFreeRegister;
                    }
                }


                while (registerValues.size() < 5) {

                }
                isProgrammingDone = true;
                //sendCards();


            }
        });

    }

    private void sendCards() {
        JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);

        int i = 0;
        for (String card :
                registerValues) {
            client.sendMessage("SelectedCard", selectedCardJsonAdapter.toJson(new SelectedCard(registerValues.get(i), i)));
            ++i;
        }
    }

    @FXML
    public int calcMaxMapHeight() {
        double centerHeight = centerStack.getHeight();
        double cardWrapperHeight = cardWrapper.getHeight();
        double maxHeight = centerHeight - cardWrapperHeight;
        return (int) maxHeight;
    }

    public int calcMaxMapWidth() {
        double centerWidth = centerStack.getWidth();
        logger.info(centerWidth);
        return (int) centerWidth;
    }

    public void cardClick() {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);

        for (StackPane card : handCards) {
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                int firstFreeRegister = registerValues.indexOf(null);
                int index = handCards.indexOf(mouseEvent.getSource());

                if (handValues.get(index) != null && !isProgrammingDone) {
                    if (firstFreeRegister != -1) {
                        // adding to register
                        registerValues.set(firstFreeRegister, handValues.get(index));
                        // adding register image
                        Image image = showCardImage(handValues.get(index));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(110);
                        imageView.setPreserveRatio(true);
                        registerCards.get(firstFreeRegister).getChildren().add(imageView);
                        // Removing from Hand
                        handValues.set(index, null);
                        // Removing handImage
                        handCards.get(index).getChildren().remove(0);
                        client.sendMessage("SelectedCard", selectedCardJsonAdapter.toJson(new SelectedCard(registerValues.get(firstFreeRegister), firstFreeRegister)));
                        onProgrammingDone();
                    } else {
                        logger.trace("Already 5 cards in the registers!");
                    }
                }
            });
        }
        for (StackPane card : registerCards) {
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                int firstFreeHand = handValues.indexOf(null);

                int index = registerCards.indexOf(mouseEvent.getSource());

                if (registerValues.get(index) != null && !isProgrammingDone) {
                    handValues.set(firstFreeHand, registerValues.get(index));
                    logger.trace("adding to hand");
                    // adding hand image
                    Image image = showCardImage(registerValues.get(index));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(110);
                    imageView.setPreserveRatio(true);
                    handCards.get(firstFreeHand).getChildren().add(imageView);
                    // Removing from register
                    registerValues.set(index, null);
                    // Removing registerImage
                    registerCards.get(index).getChildren().remove(0);
                    client.sendMessage("SelectedCard", selectedCardJsonAdapter.toJson(new SelectedCard(/*handValues.get(index)*/ "null", index)));
                }
            });
        }
    }

    @FXML
    private void handleUpgradeButton() {
        giveUpgradeToPlayer();

        JsonAdapter<BuyUpgrade> selectedCardJsonAdapter = moshi.adapter(BuyUpgrade.class);
        BuyUpgrade buyUpgrade = new BuyUpgrade(!Objects.equals(selectedUpgrade, "null"), selectedUpgrade);
        client.sendMessage("BuyUpgrade", selectedCardJsonAdapter.toJson(buyUpgrade));

        clearUpgradeCards();
        for (StackPane card :upgradeCards) {
            card.setStyle("-fx-border-width: 0px");
        }
        selectedUpgrade = "null";
        upgradeButton.setDisable(true);
        noUpgradeButton.setDisable(true);
    }

    @FXML
    private void handleNoUpgradeButton() {
        JsonAdapter<BuyUpgrade> selectedCardJsonAdapter = moshi.adapter(BuyUpgrade.class);
        BuyUpgrade buyUpgrade = new BuyUpgrade(false, "null");
        client.sendMessage("BuyUpgrade", selectedCardJsonAdapter.toJson(buyUpgrade));
        clearUpgradeCards();
        for (StackPane card :upgradeCards) {
            card.setStyle("-fx-border-width: 0px");
        }
        selectedUpgrade = "null";
        upgradeButton.setDisable(true);
        noUpgradeButton.setDisable(true);
    }

    private void giveUpgradeToPlayer() {
        if (!Objects.equals(selectedUpgrade, "null")) {
            // Differentiate if selctedCard is temporary or permanent
            if (permanentUpgradeTypes.contains(selectedUpgrade)) {
                placePermanentUpgrade();
            } else if (temporaryUpgradeTypes.contains(selectedUpgrade)) {
                placeTemporaryUpgrade();
            } else {
                throw new RuntimeException("The cardName is neither a temporary nor permanent upgrade card. ~ What is it then? " + selectedUpgrade);
            }
        }
    }

    private void placePermanentUpgrade() {
        placeUpgrade(permUpgradeValues, permUpgradeCards);
    }

    private void placeTemporaryUpgrade() {
        placeUpgrade(tempUpgradeValues, tempUpgradeCards);
    }

    private void placeUpgrade(List<String> values, List<StackPane> cards) {
        if (!values.contains("null")) {
            // TODO display message that the cards are full. Player might discharge one of the his cards.
        } else {
            int firstFreeIndex = values.indexOf("null");

            Label cardDescription = new Label(upgradeToDescription.get(selectedUpgrade));
            ImageView imageView = loadUpgradeCard(selectedUpgrade);
            StackPane cardWrapper = new StackPane(cardDescription, imageView);

            cardDescription.getStyleClass().add("upgrade-card-label");
            cardDescription.getStyleClass().add("color-light");
            cardWrapper.getStyleClass().add("upgrade-card");

            cardWrapper.setOnMouseEntered(t -> {
                imageView.setOpacity(0);
            });
            cardWrapper.setOnMouseExited(t -> {
                imageView.setOpacity(1);
            });

            if (values == tempUpgradeValues) {
                cardWrapper.setOnMouseClicked(t -> {

                    int index;
                    String cardName;

                    for (StackPane stackPane : tempUpgradeCards) {
                        if (stackPane.getChildren().contains(cardWrapper)) {
                            index = tempUpgradeCards.indexOf(cardWrapper);
                            logger.info(tempUpgradeValues.get(index));
                            cardName = tempUpgradeValues.get(index);

                            JsonAdapter<PlayCard> playCardJsonAdapter = moshi.adapter(PlayCard.class);
                            PlayCard playCard = new PlayCard(cardName);

                            String json = playCardJsonAdapter.toJson(playCard);

                            client.sendMessage("PlayCard", json);
                        }
                    }
                });
            }



            values.set(firstFreeIndex, selectedUpgrade);
            cards.get(firstFreeIndex).getChildren().add(cardWrapper);

            // Todo handle click event
        }
    }

    @FXML
    private void clearUpgradeCards() {
        logger.info(upgradeValues.size());
        upgradeValues.clear();
        logger.info(upgradeValues.size());
        for (StackPane card : upgradeCards) {
            card.getChildren().clear();
        }
    }

    @FXML
    private void handleUpgradeClick() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (StackPane card : upgradeCards) {
                    card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                        if (upgradeValues.size() != 0) {
                            for(StackPane upgradeCard : upgradeCards) {
                                upgradeCard.setStyle("-fx-border-width: 0px");
                            }
                            int index = upgradeCards.indexOf(card);
                            selectedUpgrade = upgradeValues.get(index);
                            card.setStyle("-fx-border-width: 2px");
                        }
                    });
                }
            }
        });
    }

    @FXML private void handleUpgradeHover() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (StackPane card : upgradeCards) {
                    if (card.getChildren().size() > 0) {
                        StackPane cardWrapper = (StackPane) card.getChildren().get(0);
                        ImageView imageView = (ImageView) cardWrapper.getChildren().get(1);

                    }
                }
            }
        });
    }

    public void refillShop(List<String> refillCards) {
        for (String card : refillCards) {
            upgradeValues.set(getFirstFreeUpgradeSlot(), card);
        }
    }

    private int getFirstFreeUpgradeSlot() {
        for (int i = 0; i < upgradeValues.size(); i++) {
            String value = upgradeValues.get(i);
            if (Objects.equals(value, "null")) {
                return i;
            }
        }
        return -1;
    }

    public void exchangeShop(List<String> exchangeValues) {
        logger.info("ExchangeSize " + exchangeValues);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clearUpgradeCards();

                for (int i = 0; i < 6; i++) {

                    if (i < exchangeValues.size()) {
                        String cardName = exchangeValues.get(i);
                        Label cardDescription = new Label(upgradeToDescription.get(cardName));

                        cardDescription.getStyleClass().add("upgrade-card-label");
                        cardDescription.getStyleClass().add("color-light");

                        ImageView imageView = loadUpgradeCard(cardName);

                        StackPane cardWrapper = new StackPane(cardDescription, imageView);

                        cardWrapper.getStyleClass().add("upgrade-card");

                        cardWrapper.setOnMouseEntered(t -> {
                            imageView.setOpacity(0);
                        });
                        cardWrapper.setOnMouseExited(t -> {
                            imageView.setOpacity(1);
                        });

                        upgradeValues.add(cardName);
                        upgradeCards.get(i).getChildren().add(cardWrapper);
                        logger.info("this is size of map " + upgradeValues.size());
                        logger.info("this is at i: " + upgradeValues.get(i));
                    } else {
                        upgradeValues.add("null");
                    }
                }
                upgradeButton.setDisable(false);
                noUpgradeButton.setDisable(false);
            }
        });
    }

    private ImageView loadUpgradeCard(String cardName) {
        Image image = switch (cardName) {
            case "AdminPrivilege" -> new Image(getClass().getResource("/images/card/adminPrivilege.jpg").toString());
            case "MemorySwap" -> new Image(getClass().getResource("/images/card/memorySwap.jpg").toString());
            case "RearLaser" -> new Image(getClass().getResource("/images/card/rearLaser.jpg").toString());
            case "SpamBlocker" -> new Image(getClass().getResource("/images/card/spamBlocker.jpg").toString());
            default -> new Image(getClass().getResource("/images/card/no_such_card.png").toString());
        };
        ImageView imageView = new ImageView(image);
        imageView.getStyleClass().add("upgrade-card");
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(112);

        return imageView;
    }





    @FXML
    public void updateEnergy(int energyCount){
        Platform.runLater(() -> {
            energyLabel.setText("Energy: " + energyCount);
        });
    }

    public List<String> getRegisterValues() {
        return registerValues;
    }

    public void startProgrammingPhase(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                isProgrammingDone = false;
                registerValues.clear();
                handValues.clear();

                for (StackPane card:
                        registerCards) {
                    card.getChildren().clear();
                }
                for (StackPane card:
                        handCards) {
                    card.getChildren().clear();
                }
            }
        });

    }

    @FXML
    public void onRules (){
        try {
            Desktop.getDesktop().browse(new URI("https://www.ultraboardgames.com/robo-rally/game-rules.php"));
        } catch (Exception e) {
            logger.info("Could not find the requested Website");
        }
    }

    public void startUpgradePhase(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                upgradeButton.setDisable(true);
                noUpgradeButton.setDisable(true);
            }
        });
    }

    public void setProgramDone(boolean b) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                programdone.setDisable(!b);
            }
        });
    }

    @FXML
    public void glow(){
        addBgGlow();
        addElementGlow();
    }

    @FXML
    private void addElementGlow() {
        addGlow(send_button, 0.8);
        addGlow(chat_input, 0.6);
        addGlow(upgradeButton, 0.8);
        addGlow(noUpgradeButton, 0.8);
        addGlow(programdone, 0.8);
    }

    @FXML
    private void addBgGlow() {
        DropShadow pinkGlow = new DropShadow();

        pinkGlow.setOffsetY(0f);
        pinkGlow.setOffsetX(0f);
        pinkGlow.setColor(Color.rgb(246, 1, 157));
        pinkGlow.setWidth(25);
        pinkGlow.setHeight(0);

        sidebar.setEffect(pinkGlow);
        upgradeBar.setEffect(pinkGlow);
    }

    private void addGlow(Node node, double level) {
        if (level >= 0 && level <= 1) {
            Glow glow = new Glow();
            glow.setLevel(level);
            node.setEffect(glow);
        } else {
            throw new RuntimeException("Value of level has to be a double between 0 and 1");
        }
    }
}
