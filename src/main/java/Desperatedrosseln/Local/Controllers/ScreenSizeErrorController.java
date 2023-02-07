package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javafx.scene.input.KeyCode.*;

public class ScreenSizeErrorController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private VBox overlay;

    @FXML
    private Button closeButton;

    @FXML
    private Label errorMessage;

    @FXML private TextField input;

    @FXML private Button send_button;

    @FXML private HBox inputContainer;

    private final String dev = "DNSTPWA";

    public ScreenSizeErrorController() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/screenSizeErrorScene.fxml"));
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

    public void startScene(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        stage.setMinHeight(389);
        stage.setMinWidth(512);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
        handleDevCombination();
        glow();
    }

    @FXML
    private void handleDevCombination() {
        KeyCombination keyCombination = new KeyCodeCombination(KeyCode.F10, KeyCombination.SHORTCUT_DOWN);
        Runnable runnable = ()-> {
            openDevTool();
        };
        scene.getAccelerators().put(keyCombination, runnable);
    }

    @FXML
    private void openDevTool() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(45,226,230));
        glow.setWidth(45);
        glow.setHeight(2);
        overlay.setStyle("-fx-border-color: rgb(45,226,230)");
        overlay.setEffect(glow);
        inputContainer.setOpacity(1);
    }

    @FXML
    public void onClose(ActionEvent event) {
        stage.close();
    }

    @FXML
    public void checkDevPassword(ActionEvent event) {
        String value = input.getText();

        if (Objects.equals(value, dev)) {
            LoginController loginController = new LoginController();
            loginController.startLoginScene(stage);
        }
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
        addGlow(closeButton, 0.8);
        addGlow(errorMessage, 0.4);

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
