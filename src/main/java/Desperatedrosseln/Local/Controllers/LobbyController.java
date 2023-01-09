package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML
    private Label playersonline;

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
        mainController = new MainController();
        client.setMainController(mainController);
        client.sendHelloServer();
        playersonline.setText("Players currently in lobby: " );
        this.stage = stage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onButtonClicked(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();


        int selectedRobot = 0;
        switch (clickedButton.getId()) {
            case "player-icon--1":
                System.out.println("Brown WAS PRESSED");
                selectedRobot = 1;
                break;
            case "player-icon--2":
                System.out.println("Yellow WAS PRESSED");
                selectedRobot = 2;
                break;
            case "player-icon--3":
                System.out.println("Blue was Pressed");
                selectedRobot = 3;
                break;
            case "player-icon--4":
                System.out.println("Green was Pressed");
                selectedRobot = 4;
                break;
            case "player-icon--5":
                System.out.println("Orange was pressed");
                selectedRobot = 5;
                break;
            case "player-icon--6":
                System.out.println("Red was pressed");
                selectedRobot = 6;
                break;
        }

        if(!client.getRobotIDs().contains(selectedRobot)){
            mainController.setSelectedRobot(selectedRobot);
            mainController.startMainScene(stage, selectedRobot);
        }

    }


}

