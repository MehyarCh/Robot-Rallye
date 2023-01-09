package Desperatedrosseln.Local.Controllers;

//import Desperatedrosseln.Local.Client;

import Desperatedrosseln.Local.Protocols.SelectedCard;
import Desperatedrosseln.Logic.Elements.MapField;
import Desperatedrosseln.Local.CardLabels.*;
import Desperatedrosseln.Local.Client;
import Desperatedrosseln.Logic.Cards.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static Desperatedrosseln.Local.Controllers.LoginController.client;

public class MainController {

    DataOutputStream dos;
    DataInputStream dis;


    private Thread thread;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private MapController mapController;

    List<List<MapField>> mapFields;

    ArrayList<String> handValues = new ArrayList<>();
    ArrayList<String> registerValues = new ArrayList<>();
    @FXML
    private GridPane mapGrid;

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
    private ArrayList<StackPane> registerCards = new ArrayList<>();
    private ArrayList<StackPane> handCards = new ArrayList<>();
    private int registerTrack=0;
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


    /*EventHandler clickCard = (evt) -> {

        Label selectedCard = (Label) evt.getSource();
        //check if card is in hand, then add it to the next free register slot
        if (checkCard(selectedCard)) {
            for (int j = 0; j < registerCards.size(); j++) {
                //check if the registerCard StackPane only has one label (cardarea) in it
                if (registerCards.get(j).getChildren().size() == 1) {
                    registerCards.get(j).getChildren().add(selectedCard);
                    System.out.println("Card added to register j: " + j);
                    System.out.println(handCards.get(j).getChildren());
                    System.out.println(registerCards.get(j).getChildren());
                    return;
                }


            }
            //when card is in the register, add it back to the hand
        } else {
            for (int k = 0; k < handCards.size(); k++) {
                if (handCards.get(k).getChildren().size() == 1) {
                    handCards.get(k).getChildren().add(selectedCard);
                    System.out.println("Card added to hand k: " + k);
                    return;
                }
            }
        }

    };

    //Check if a card is in the hand of a player -> true
    private boolean checkCard(Label card) {
        for (int i = 0; i < handCards.size(); i++) {
            if (handCards.get(i).getChildren().contains(card)) {
                System.out.println("Card is in hand");
                return true;
            }
        }
        System.out.println("Card is not in hand");
        return false;
    }*/

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

    public MapController getMapController(){
        return mapController;
    }

    public void startMainScene(Stage stage) throws IOException {
        this.stage = stage;

        mapController = new MapController(mapGrid);
        client.sendPlayerValues(0);
        // mapController.showMap("dizzyHighway");
        fillDummyHand();
        initRegisterValues();
        updateCardImages();

        stage.setScene(scene);
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();

        //ToDo change it

        for (StackPane card : handCards) {
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                int firstFreeRegister = registerValues.indexOf(null);
                int firstFreeHand = handValues.indexOf(null);


                if (mouseEvent.getSource() == handCardOne ||
                        mouseEvent.getSource() == handCardTwo ||
                        mouseEvent.getSource() == handCardThree ||
                        mouseEvent.getSource() == handCardFour ||
                        mouseEvent.getSource() == handCardFive ||
                        mouseEvent.getSource() == handCardSix ||
                        mouseEvent.getSource() == handCardSeven ||
                        mouseEvent.getSource() == handCardEight ||
                        mouseEvent.getSource() == handCardNine
                ) {
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
                        } else {
                            System.out.println("Already 5 cards in the registers!");
                        }
                    }
                } else {
                    // adding to hand
                    int index = registerCards.indexOf(mouseEvent.getSource());
                    if (registerValues.get(index) != null) {
                        handValues.set(firstFreeHand, registerValues.get(index));
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
                    }
                }
            });
        }
        for (StackPane card : registerCards) {
            card.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                int firstFreeRegister = registerValues.indexOf(null);
                int firstFreeHand = handValues.indexOf(null);


                if (mouseEvent.getSource() == handCardOne ||
                        mouseEvent.getSource() == handCardTwo ||
                        mouseEvent.getSource() == handCardThree ||
                        mouseEvent.getSource() == handCardFour ||
                        mouseEvent.getSource() == handCardFive ||
                        mouseEvent.getSource() == handCardSix ||
                        mouseEvent.getSource() == handCardSeven ||
                        mouseEvent.getSource() == handCardEight ||
                        mouseEvent.getSource() == handCardNine
                ) {
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
                        } else {
                            System.out.println("Already 5 cards in the registers!");
                        }
                    }
                } else {
                    // adding to hand
                    int index = registerCards.indexOf(mouseEvent.getSource());
                    if (registerValues.get(index) != null) {
                        handValues.set(firstFreeHand, registerValues.get(index));
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
                    }
                }
            });
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
        int i = 0;

        for (String cardValue : handValues) {
            ImageView stackElement = new ImageView(showCardImage(cardValue));
            stackElement.setPreserveRatio(true);
            stackElement.setFitHeight(110);
            handCards.get(i++).getChildren().add(stackElement);
        }
    }

    public Image showCardImage(String cardValue) {
        return switch (cardValue) {
            case "Move1" -> new Image(getClass().getResource("/images/Card/move1.jpg").toString());
            case "Move2" -> new Image(getClass().getResource("/images/Card/move2.jpg").toString());
            case "LeftTurn" -> new Image(getClass().getResource("/images/Card/leftTurn.jpg").toString());
            case "Move3" -> new Image(getClass().getResource("/images/Card/move3.jpg").toString());
            case "RightTurn" -> new Image(getClass().getResource("/images/Card/rightTurn.jpg").toString());
            case "U-Turn" -> new Image(getClass().getResource("/images/Card/u-turn.jpg").toString());
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
        //TODO: send register list to server
        //TODO: send cards left in hand to server

        ArrayList<String> cards = new ArrayList<>();
        cards.add("RightTurn");
        cards.add("Move1");
        cards.add("LeftTurn");
        cards.add("UTurn");
        cards.add("Move3");
        //ToDo change back to register

        if(cards.size()==5){
            Moshi moshi = new Moshi.Builder().build();

            JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);
            for(int i=0 ; i<5 ; ++i){
               client.sendMessage("SelectedCard",selectedCardJsonAdapter.toJson(new SelectedCard(cards.get(i),i)));
            }
        }


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
}
