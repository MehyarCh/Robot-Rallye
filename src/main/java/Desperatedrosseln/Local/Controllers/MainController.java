package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.CardLabels.*;
import Desperatedrosseln.Local.Client;
import Desperatedrosseln.Logic.Cards.*;
import Desperatedrosseln.Logic.Cards.Programming.*;
import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class MainController {
    DataOutputStream dos;
    DataInputStream dis;

    private Thread thread;
    private Stage stage;
    private Scene scene;
    private Parent root;
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
    private ArrayList<StackPane> registerCards;
    private ArrayList<Label> cardLabels;

    private ArrayList<StackPane> handCards;
    private MoveOneLabel moveOneLabel;

    private MoveTwoLabel moveTwoLabel;

    private MoveThreeLabel moveThreeLabel;

    private MoveBack moveBackLabel;

    private Again againLabel;

    private PowerUp powerUpLabel;

    private TurnLeft turnLeftLabel;

    private TurnRight turnRightLabel;

    private UTurn uTurnLabel;

    EventHandler handler = (evt) -> {

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

    public MainController() throws IOException {
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

            registerCards = new ArrayList<>();
            registerCards.add(registerCardOne);
            registerCards.add(registerCardTwo);
            registerCards.add(registerCardThree);

            handCards = new ArrayList<>();
            handCards.add(handCardOne);
            handCards.add(handCardTwo);
            handCards.add(handCardThree);

            moveOneLabel = new MoveOneLabel(handler);
            moveTwoLabel = new MoveTwoLabel(handler);
            moveThreeLabel = new MoveThreeLabel(handler);

            cardLabels = new ArrayList<>();
            cardLabels.add(moveOneLabel.getCardLabel());
            cardLabels.add(moveTwoLabel.getCardLabel());
            cardLabels.add(moveThreeLabel.getCardLabel());

            handCardOne.getChildren().add(moveOneLabel.getCardLabel());
            handCardTwo.getChildren().add(moveTwoLabel.getCardLabel());
            handCardThree.getChildren().add(moveThreeLabel.getCardLabel());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startMainScene(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
    }

    @FXML
    public void onMessageSend(KeyEvent event){
        if(event.getCode().toString().equals("ENTER"))
        {
            onClickSend();
        }
    }
    @FXML
    void onClickSend() {

            String msg = chat_input.getText();

            System.out.println(LoginController.client.getName()+ ": "+ msg);

            LoginController.client.sendChatMessage(msg, -1);

            chat_input.setText("");
            chat_input.requestFocus();


    }

    @FXML
    public void addChatMessage(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatlog.getChildren().add(new Text( message + "\n"));

            }
        });

    }
    private void setStreams(){
        this.dos = LoginController.client.getOutputStr();
        this.dis = LoginController.client.getInputStr();
    }

}
