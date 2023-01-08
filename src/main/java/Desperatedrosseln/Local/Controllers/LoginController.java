package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class LoginController {

    private Thread thread;
    public static Client client;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public LobbyController lobbyController;

    @FXML
    public Button joinButton;

    @FXML
    public TextField loginTextField;
    @FXML
    private Label loginwarning;




    public LoginController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/loginScene.fxml"));
        loader.setController(this);

        try {
            root = loader.load();
            scene = new Scene(root);

            String mainCss = this.getClass().getResource("/Css/main.css").toExternalForm();
            scene.getStylesheets().add(mainCss);

            String modulesCss = this.getClass().getResource("/Css/modules.css").toExternalForm();
            scene.getStylesheets().add(modulesCss);

            String stateCss = this.getClass().getResource("/Css/state.css").toExternalForm();
            scene.getStylesheets().add(stateCss);;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startLoginScene(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void onLogin(ActionEvent event) throws IOException {
        if (loginTextField.getText().isBlank()){
            loginTextField.setStyle(String.valueOf(loginwarning));
            loginwarning.setText("Please write your name to continue");
        }else {

            client = new Client();
            client.setClientName(loginTextField.getText());
            switchToLobbyScene();
            client.sendHelloServer();
            System.out.println("Hello welcome " + loginTextField.getText());
        }

    }
    public void switchToLobbyScene() throws IOException {
        lobbyController = new LobbyController();
        lobbyController.startLobbyScene(stage);
    }



}
