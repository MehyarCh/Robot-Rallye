package Desperatedrosseln;

import Desperatedrosseln.Local.Controllers.LoginController;
import Desperatedrosseln.Local.Controllers.MainController;
import Desperatedrosseln.Local.Controllers.LobbyController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application implements Runnable {

    // sceneNumber 0: mainScene, 1: loginScene, 2: lobbyScene
    @Override
    public void start(Stage primaryStage) {
        try {
            launchScene(primaryStage, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchScene(Stage stage, int sceneNumber) throws IOException {
        if (sceneNumber == 0) {
            MainController mainController = new MainController();
            //mainController.startMainScene(stage);
        } else if (sceneNumber == 1) {
            LoginController loginController = new LoginController();
            loginController.startLoginScene(stage);
        } else if (sceneNumber == 2) {
            LobbyController lobbyController = new LobbyController();
            lobbyController.startLobbyScene(stage);
        }
    }
    @Override
    public void run(){launch();}

}
