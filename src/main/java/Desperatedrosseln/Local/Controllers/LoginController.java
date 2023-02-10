package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
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
    private Label loginFormLabel;

    @FXML
    public TextField loginTextField;
    @FXML
    private Label loginwarning;

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private Label loginWarning;



    public LoginController() {

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
            scene.getStylesheets().add(stateCss);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startLoginScene(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        stage.setMinHeight(768);
        stage.setMinWidth(1024);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("RoboRally");
        stage.show();
        glow();
    }

    @FXML
    public void glow(){
        addBgGlow();
        addElementGlow();
    }

    @FXML
    private void addBgGlow() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.rgb(246, 1, 157));
        borderGlow.setWidth(45);
        borderGlow.setHeight(2);
        overlay.setEffect(borderGlow);
    }

    @FXML
    private void addElementGlow() {
        addGlow(joinButton, 0.8);
        addGlow(loginFormLabel, 0.4);
        addGlow(loginTextField, 0.8);
        addGlow(loginWarning, 0.8);

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



    @FXML
    public void onLogin(ActionEvent event) throws IOException {
        if (loginTextField.getText().isBlank()){
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