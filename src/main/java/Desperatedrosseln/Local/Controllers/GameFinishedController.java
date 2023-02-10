package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GameFinishedController {

    private Thread thread;
    public static Client client;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public LobbyController lobbyController;

    @FXML
    private StackPane loginContainer;

    @FXML
    private VBox overlay;

    private static final Logger logger = LogManager.getLogger(LoginController.class);


    public GameFinishedController() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/gameFinished.fxml"));
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

    public void startGameFinishedScene(Stage stage) {
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
    public void glow() {
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
}
