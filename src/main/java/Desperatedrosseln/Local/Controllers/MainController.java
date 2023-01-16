package Desperatedrosseln.Local.Controllers;

//import Desperatedrosseln.Local.Client;

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
    @FXML
    private GridPane mapGrid;
    @FXML
    private GridPane cardWrapper;
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
    private ArrayList<StackPane> registerCards = new ArrayList<>();
    private ArrayList<StackPane> handCards = new ArrayList<>();
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
    @FXML
    private Label timeLabel;


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

            //TESTING THE MOVE CARDS FUNCTION

            //List of the StackPanes which represent the register
            registerCards.add(registerCardOne);
            registerCards.add(registerCardTwo);
            registerCards.add(registerCardThree);
            registerCards.add(registerCardFour);
            registerCards.add(registerCardFive);

            //List of the StackPanes which represent the handcards
            handCards.add(handCardOne);
            handCards.add(handCardTwo);
            handCards.add(handCardThree);
            handCards.add(handCardFour);
            handCards.add(handCardFive);
            handCards.add(handCardSix);
            handCards.add(handCardSeven);
            handCards.add(handCardEight);
            handCards.add(handCardNine);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public MapController getMapController() {
        return mapController;
    }

    public void startMainScene(int selectedRobot) throws IOException {
        client.isMainSceneStarted = true;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage = new Stage();
                stage.setScene(scene);
                stage.setMinHeight(720);
                stage.setMinWidth(1280);
                stage.setMaximized(true);
                stage.setResizable(true);

                mapController = new MapController(mapGrid, selectedRobot, calcMaxMapHeight());
                mapController.setClient(client);
                stage.show();
                startTimer();
            }


        });
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
                                System.out.println(Thread.currentThread().getName());
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
                    System.out.println(stackElement.toString());
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
            case "Move" -> new Image(getClass().getResource("/images/Card/move.jpg").toString());
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
            sendCards();
            isProgrammingDone = true;
        }
        programdone.setDisable(true);
    }

    @FXML
    void onClickSend() {

        if (!chat_input.getText().isEmpty()) {
            String msg = chat_input.getText();

            //System.out.println(client.getName()+ ": "+ msg);

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
        System.out.println(handValues);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {


                for (int firstFreeRegister = 0; firstFreeRegister < 5; firstFreeRegister++) {
                    if (registerValues.get(firstFreeRegister) == null) {
                        int index = (int) (Math.random() * handValues.size());
                        System.out.println("index=" + index + ", ffr=" + firstFreeRegister);
                        if (index == handValues.size()) {
                            --index;
                        }
                        if (handValues.get(index) != null) {

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


                        }
                        --firstFreeRegister;
                    }
                }


                while (registerValues.size() < 5) {

                }
                isProgrammingDone = true;
                sendCards();
                programdone.setDisable(true);

            }
        });

    }

    private void sendCards() {
        Moshi moshi = new Moshi.Builder().build();

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
        System.out.println(stage.getHeight() + " - " + scene.getRoot().getChildrenUnmodifiable().get(0).getScaleY() + "-" + cardWrapper.getHeight() + "-100");
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
                        System.out.println("added to register 1");
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
                        System.out.println("Already 5 cards in the registers!");
                    }
                }
            });
        }
        for (StackPane card : registerCards) {
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                System.out.println(handValues);
                int firstFreeHand = handValues.indexOf(null);

                System.out.println("This is a registerCard");
                int index = registerCards.indexOf(mouseEvent.getSource());

                if (registerValues.get(index) != null) {
                    handValues.set(firstFreeHand, registerValues.get(index));
                    System.out.println("adding to hand");
                    // adding hand image
                    Image image = showCardImage(registerValues.get(index));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(110);
                    imageView.setPreserveRatio(true);
                    handCards.get(firstFreeHand).getChildren().add(imageView);
                    System.out.println("added image");
                    // Removing from register
                    registerValues.set(index, null);
                    System.out.println("removed from register");
                    // Removing registerImage
                    registerCards.get(index).getChildren().remove(0);
                    System.out.println("removed image");
                    client.sendMessage("SelectedCard", selectedCardJsonAdapter.toJson(new SelectedCard(handValues.get(firstFreeHand), firstFreeHand)));
                }
            });
        }
    }
}
