package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.Client;
import Desperatedrosseln.Logic.Cards.Programming.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
    private ImageView cardOne;
    @FXML
    private ImageView Hundfunktionier;



    public void handCardClicked(MouseEvent event){

        if (event.getSource().equals(cardOne)) {
            cardOne.setImage(new Image("file:cardtest2.png"));
            System.out.println("test");
        }
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
    @FXML
    private void moveCard(MouseEvent event){

    }





}
