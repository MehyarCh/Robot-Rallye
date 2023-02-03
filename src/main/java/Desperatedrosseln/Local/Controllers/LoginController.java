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
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginController {

    private Thread thread;
    public static Client client;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public LobbyController lobbyController;

    @FXML
    private StackPane loginContainer;

    //@FXML
    //private ImageView background;


    @FXML
    private VBox overlay;

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
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
        addGlow();
    }

    public void addGlow(){
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.rgb(246, 1, 157));
        borderGlow.setWidth(45);
        borderGlow.setHeight(4);
        overlay.setEffect(borderGlow);

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