package Desperatedrosseln.Local.Controllers;

//import Desperatedrosseln.Local.Client;

import Desperatedrosseln.Local.Protocols.BuyUpgrade;
import Desperatedrosseln.Local.Protocols.SelectedCard;
import Desperatedrosseln.Local.CardLabels.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

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

    private HashMap<Integer, String> upgradePositionToValue = new HashMap<>();

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
    private Button upgradeButton;

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

    private List<StackPane> registerCards;
    private List<StackPane> handCards;
    private List<StackPane> upgradeCards;
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
    private HBox upgradeBar;

    @FXML
    private GridPane sidebar;

    @FXML private GridPane navbar;

    private static final Logger logger = LogManager.getLogger(MainController.class);


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
                //startTimer();
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
    void showOverlay(String content) {
        HBox overlay = new HBox();
        Label label = new Label(content);

        label.getStyleClass().add("main-overlay-label");
        overlay.getStyleClass().add("main-overlay");

        overlay.getChildren().add(label);

        centerStack.getChildren().add(overlay);
    }

    @FXML
    private void hideOverlay() {
        centerStack.getChildren().remove(centerStack.getChildren().size() - 1);
    }


    public void startTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            private int seconds = 30;

            @Override
            public void run() {
                Platform.runLater(() -> {
                            timeLabel.setText(String.valueOf(seconds--));
                            if (seconds < 0) {
                                timer.cancel();
                            }
                        }
                );
            }
        }, 0, 1000);
    }

    public void fillHand() {
        for (String text : client.getCardsInHand()) {
            handValues.add(text);
        }
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
            registerValues.size();
        }
    }

    public void updateCardImages() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (String cardValue : handValues) {
                    ImageView stackElement = new ImageView(showCardImage(cardValue));
                    stackElement.setPreserveRatio(true);
                    stackElement.setFitHeight(110);
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
            case "Spam" -> new Image(getClass().getResource("/images/card/no_such_card.png").toString()); //ToDo: change picture
            case "Virus" -> new Image(getClass().getResource("/images/card/no_such_card.png").toString());
            case "Trojan" -> new Image(getClass().getResource("/images/card/no_such_card.png").toString());
            case "Worm" -> new Image(getClass().getResource("/images/card/no_such_card.png").toString());
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

                logger.info("sssssssssssss" + message);

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
        JsonAdapter<BuyUpgrade> selectedCardJsonAdapter = moshi.adapter(BuyUpgrade.class);
        BuyUpgrade buyUpgrade = new BuyUpgrade(!Objects.equals(selectedUpgrade, "null"), selectedUpgrade);
        client.sendMessage("BuyUpgrade", selectedCardJsonAdapter.toJson(buyUpgrade));
        clearUpgradeCards();
        upgradeButton.setDisable(true);
        //ToDo: empty shop, check Energy Reserve before buying
    }

    @FXML
    private void clearUpgradeCards() {
        upgradePositionToValue.clear();
        for (StackPane card : upgradeCards) {
            card.getChildren().clear();
        }
    }

    @FXML
    private void handleUpgradeClick() {
        for (StackPane card : upgradeCards) {
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                for(StackPane upgradeCard : upgradeCards) {
                    upgradeCard.setStyle("-fx-border-width: 0px");
                }

                int index = upgradeCards.indexOf(card);
                if (Objects.equals(selectedUpgrade, "null")) {
                    selectedUpgrade = upgradePositionToValue.get(index);
                    card.setStyle("-fx-border-width: 2px");
                    logger.debug(upgradePositionToValue.get(index));
                } else {
                    upgradePositionToValue.put(index, "null");
                    card.setStyle("-fx-border-width: 2px");
                    logger.debug(upgradePositionToValue.get(index));
                }
                logger.debug(selectedUpgrade);
            });
        }
    }

    public void refillShop(List<String> refillCards) {
        for (String card : refillCards) {
            upgradePositionToValue.put(getFirstFreeUpgradeSlot(), card);
        }
    }

    private int getFirstFreeUpgradeSlot() {
        for (int i = 0; i < upgradePositionToValue.size(); i++) {
            String value = upgradePositionToValue.get(i);
            if (Objects.equals(value, "null")) {
                return i;
            }
        }
        return -1;
    }

    public void exchangeShop(List<String> exchangeValues) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < exchangeValues.size(); i++) {
                    String cardName = exchangeValues.get(i);
                    ImageView imageView = loadUpgradeCard(cardName);
                    imageView.setFitHeight(90);
                    imageView.setPreserveRatio(true);
                    imageView.setOpacity(0.8);
                    upgradeCards.get(i).getChildren().add(imageView);

                    upgradePositionToValue.put(i, cardName);
                }
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
        return new ImageView(image);
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

    public void startUpgradePhase(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                upgradeButton.setDisable(false);
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
