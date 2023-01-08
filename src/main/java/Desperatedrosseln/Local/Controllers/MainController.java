package Desperatedrosseln.Local.Controllers;

//import Desperatedrosseln.Local.Client;

import Desperatedrosseln.Logic.Elements.MapField;
import Desperatedrosseln.Local.CardLabels.*;
import Desperatedrosseln.Local.Client;
import Desperatedrosseln.Logic.Cards.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
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


    ArrayList<String> handCards1 = new ArrayList<>();
    ArrayList<String> registerCards1 = new ArrayList<>();
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
    private ArrayList<StackPane> registerCards;
    private int registerTrack=0;
    private ArrayList<StackPane> handCards;
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


    EventHandler clickCard = (evt) -> {

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
    }

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
            registerCards = new ArrayList<>();
            registerCards.add(registerCardOne);
            registerCards.add(registerCardTwo);
            registerCards.add(registerCardThree);
            registerCards.add(registerCardFour);
            registerCards.add(registerCardFive);

            //List of the StackPanes which represent the handcards
            handCards = new ArrayList<>();
            handCards.add(handCardOne);
            handCards.add(handCardTwo);
            handCards.add(handCardThree);
            handCards.add(handCardFour);
            handCards.add(handCardFive);
            handCards.add(handCardSix);
            handCards.add(handCardSeven);
            handCards.add(handCardEight);
            handCards.add(handCardNine);
            new Image(getClass().getResource("/images/card/move.jpg").toString());
            //creating the cardLabels (images) and adding a clickCard which is the MouseEvent
            moveOneLabel = new MoveOneLabel(clickCard);
            moveTwoLabel = new MoveTwoLabel(clickCard);
            anotherMoveOneLabel = new MoveOneLabel(clickCard);
            moveThreeLabel = new MoveThreeLabel(clickCard);
            //adding the labels

            handCardOne.getChildren().add(moveOneLabel.getCardLabel());
            handCardThree.getChildren().add(anotherMoveOneLabel.getCardLabel());
            handCardThree.getChildren().add(moveThreeLabel.getCardLabel());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startMainScene(Stage stage) throws IOException {
        this.stage = stage;


        // mapController.showMap("dizzyHighway");

        stage.setScene(scene);
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
        mapController = new MapController(mapGrid);
        client.sendPlayerValues(0);
        //ToDo change it
        showCardImage();
    }

    @FXML
    public void onMessageSend(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            onClickSend();
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

    public MapController getMapController() {
        return mapController;
    }

    public void showCardImage() {
        Image img;
        int i = 0;
        //for(String card: client.getCardsInHand()){
        ArrayList<String> cards = new ArrayList<>();
        cards.add("RightTurn");
        cards.add("");
        cards.add("Move1");
        cards.add("LeftTurn");
        cards.add("U-Turn");
        cards.add("");
        cards.add("");
        cards.add("Move3");
        cards.add("Move3");
        for (String card : cards) {
            ImageView stackElement = null;
            switch (card) {
                case "Move1":
                    img = new Image(getClass().getResource("/images/Card/move1.jpg").toString());
                    break;
                case "Move2":
                    img = new Image(getClass().getResource("/images/Card/move2.jpg").toString());
                    break;
                case "LeftTurn":
                    img = new Image(getClass().getResource("/images/Card/leftTurn.jpg").toString());
                    break;
                case "Move3":
                    img = new Image(getClass().getResource("/images/Card/move3.jpg").toString());
                    break;
                case "RightTurn":
                    img = new Image(getClass().getResource("/images/Card/rightTurn.jpg").toString());
                    break;
                case "U-Turn":
                    img = new Image(getClass().getResource("/images/Card/u-turn.jpg").toString());
                    break;
                case "Again":
                    img = new Image(getClass().getResource("/images/Card/again.jpg").toString());
                    break;
                case "PowerUp":
                    img = new Image(getClass().getResource("/images/Card/powerup.jpg").toString());
                    break;
                case "MoveBack":
                    img = new Image(getClass().getResource("/images/Card/moveback.jpg").toString());
                    break;
                case "Move":
                    img = new Image(getClass().getResource("/images/Card/move.jpg").toString());
                    break;
                default:
                    img = new Image(getClass().getResource("/images/Card/no_such_card.png").toString());
            }
            stackElement = new ImageView(img);
            stackElement.setPreserveRatio(true);
            stackElement.setFitHeight(110);
            handCards.get(i++).getChildren().add(stackElement);

        }

    }

    public void selectHand(StackPane card){
        registerCards.get(registerTrack++).getChildren().add(card.getChildren().get(0));
        card.getChildren().removeAll();
        registerTrack%=5;
    }


}
