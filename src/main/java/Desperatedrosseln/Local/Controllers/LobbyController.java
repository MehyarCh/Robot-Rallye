package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;

import static Desperatedrosseln.Local.Controllers.LoginController.client;

public class LobbyController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button playerIcon1;
    @FXML
    private Button playerIcon2;
    @FXML
    private Button playerIcon3;
    @FXML
    private Button playerIcon4;
    @FXML
    private Button playerIcon5;
    @FXML
    private Button playerIcon6;

    public MainController mainController;


    private TextFlow textFlow;

    @FXML
    private Button playerIconPink;

    public LobbyController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/lobbyScene.fxml"));
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

    public void startLobbyScene(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onButtonClicked(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        mainController = new MainController();
        mainController.startMainScene(stage);
        client.setMainController(mainController);
        mainController.startMainScene(stage);

        switch (clickedButton.getId()) {
            case "player-icon--1":
                System.out.println("PINK WAS PRESSED");
                break;
            case "player-icon--2":
                System.out.println("Yellow WAS PRESSED");
                break;
            case "player-icon--3":
                System.out.println("Blue was Pressed");
                break;
            case "player-icon--4":
                System.out.println("Green was Pressed");
                break;
            case "player-icon--5":
                System.out.println("Orange was pressed");
                break;
            case "player-icon--6":
                System.out.println("Red was pressed");
                break;
        }
    }


}

