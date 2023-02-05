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
import javafx.scene.image.*;
import javafx.stage.Stage;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginController {

    private Thread thread;
    public static Client client;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public LobbyController lobbyController;

    public Image applicationImage;
    @FXML
    public Button joinButton;

    @FXML
    public TextField loginTextField;
    @FXML
    private Label loginwarning;

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private Label loginWarning;

    public LoginController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginScene.fxml"));
        loader.setController(this);

        try {
            root = loader.load();
            scene = new Scene(root);

            String mainCss = this.getClass().getResource("/css/main.css").toExternalForm();
            scene.getStylesheets().add(mainCss);

            String modulesCss = this.getClass().getResource("/css/modules.css").toExternalForm();
            scene.getStylesheets().add(modulesCss);

            String stateCss = this.getClass().getResource("/css/state.css").toExternalForm();
            scene.getStylesheets().add(stateCss);;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startLoginScene(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        //stage.getIcons().add(new Image(getClass().getResource("images/robots/orange.jpg").toString()));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onLogin(ActionEvent event) throws IOException {
        if (loginTextField.getText().isBlank()){
            //loginTextField.setStyle(String.valueOf(loginWarning));
            loginWarning.setText("Your name cannot be empty!");
        }else {

            client = new Client();
            client.setClientName(loginTextField.getText());
            switchToLobbyScene();

            logger.info(loginTextField.getText() + " joined the Lobby");

        }

    }
    public void switchToLobbyScene() throws IOException {
        lobbyController = new LobbyController();
        lobbyController.startLobbyScene(stage);
    }



}
