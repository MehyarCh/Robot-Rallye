package Desperatedrosseln.Local.Controllers;

//import Desperatedrosseln.Local.Client;

import Desperatedrosseln.Local.Protocols.BuyUpgrade;
import Desperatedrosseln.Local.Protocols.SelectedCard;
import Desperatedrosseln.Local.CardLabels.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
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

    private static final Logger logger = LogManager.getLogger(MainController.class);


    public MainController() {

        setStreams();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainScene.fxml"));
        loader.setController(this);

        try {
            root = loader.load();
            scene = new Scene(root);

            String mainCss = this.getClass().getResource("/Css/main.css").toExternalForm();
            scene.getStylesheets().add(mainCss);

            String modulesCss = this.getClass().getResource("/Css/modules.css").toExternalForm();
            scene.getStylesheets().add(modulesCss);


            String stateCss = this.getClass().getResource("/Css/state.css").toExternalForm();
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
        mapController = new MapController(mapGrid, selectedRobot, calcMaxMapHeight());
        mapController.setClient(client);
        setProfileIcon();
        handleUpgradeClick();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setScene(scene);
                stage.setMinHeight(720);
                stage.setMinWidth(1280);
                stage.setMaximized(true);
                stage.setResizable(true);
                stage.show();
                startTimer();
            }
        });
    }

    @FXML
    private void setProfileIcon(){
        switch (selectedRobot){
            case 1:
                profileIcon.setId("player-icon--1");
                break;
            case 2:
                profileIcon.setId("player-icon--2");
                break;
            case 3:
                profileIcon.setId("player-icon--3");
                break;
            case 4:
                profileIcon.setId("player-icon--4");
                break;
            case 5:
                profileIcon.setId("player-icon--5");
                break;
            case 6:
                profileIcon.setId("player-icon--6");
                break;

        }
    }
    @FXML void showOverlay(String content) {
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
                            if (seconds < 0 || mapController.hasStartpoint) {
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
            case "MoveOne" -> new Image(getClass().getResource("/images/Card/move1.jpg").toString());
            case "MoveTwo" -> new Image(getClass().getResource("/images/Card/move2.jpg").toString());
            case "TurnLeft" -> new Image(getClass().getResource("/images/Card/leftTurn.jpg").toString());
            case "MoveThree" -> new Image(getClass().getResource("/images/Card/move3.jpg").toString());
            case "TurnRight" -> new Image(getClass().getResource("/images/Card/rightTurn.jpg").toString());
            case "UTurn" -> new Image(getClass().getResource("/images/Card/u-turn.jpg").toString());
            case "Again" -> new Image(getClass().getResource("/images/Card/again.jpg").toString());
            case "PowerUp" -> new Image(getClass().getResource("/images/Card/powerup.jpg").toString());
            case "MoveBack" -> new Image(getClass().getResource("/images/Card/moveback.jpg").toString());
            default -> new Image(getClass().getResource("/images/Card/no_such_card.png").toString());
        };
    }

    @FXML
    public void onMessageSend(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            onClickSend();
        }
    }

    @FXML
    public void onProgrammingDone() {
        if (registerValues.size() == 5) {
            //sendCards();
            isProgrammingDone = true;
            programdone.setDisable(true);
            logger.info(client.getName() + " is done programming");
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
                chatlog.getChildren().add(new Text(message + "\n"));
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
                programdone.setDisable(true);

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
    int calcMaxMapHeight() {
        return (int) (stage.getHeight() - scene.getRoot().getChildrenUnmodifiable().get(0).getScaleY() - cardWrapper.getHeight() - 100);
    }

    public void cardClick() {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);

        for (StackPane card : handCards) {
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                int firstFreeRegister = registerValues.indexOf(null);
                int index = handCards.indexOf(mouseEvent.getSource());

                if (handValues.get(index) != null) {
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

                if (registerValues.get(index) != null) {
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
    }

    @FXML
    private void handleUpgradeClick() {
        for (StackPane card : upgradeCards) {
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                if (card.getChildren().size() > 0) {
                    int index = upgradeCards.indexOf(card);
                    if (Objects.equals(selectedUpgrade, upgradePositionToValue.get(index))) {
                        upgradePositionToValue.put(index, "null");
                        upgradeButton.setDisable(true);
                    } else {
                        selectedUpgrade = upgradePositionToValue.get(index);
                        if (upgradeButton.isDisabled()) {
                            upgradeButton.setDisable(false);
                        }
                    }
                    logger.debug(selectedUpgrade);
                }
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
        upgradePositionToValue.clear();

        for (int i = 0; i < exchangeValues.size(); i++) {
            String cardName = exchangeValues.get(i);
            ImageView imageView = loadUpgradeCard(cardName);
            imageView.setFitHeight(90);
            imageView.setPreserveRatio(true);
            upgradeCards.get(i).getChildren().add(imageView);

            upgradePositionToValue.put(i, cardName);
        }
    }

    private ImageView loadUpgradeCard(String cardName) {
        Image image = switch (cardName) {
            case "adminPrivilege" -> new Image(getClass().getResource("/images/Card/adminPrivilege.jpg").toString());
            case "memoryBlocker" -> new Image(getClass().getResource("/images/Card/memoryBlocker.jpg").toString());
            case "rearLaser" -> new Image(getClass().getResource("/images/Card/rearLaser.png").toString());
            case "spamBlocker" -> new Image(getClass().getResource("/images/Card/spamBlocker.png").toString());
            default -> new Image(getClass().getResource("/images/Card/no_such_card.png").toString());
        };
        return new ImageView(image);
    }
}
