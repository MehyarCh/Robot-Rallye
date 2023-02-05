package Desperatedrosseln;

import Desperatedrosseln.Local.Controllers.LoginController;
import Desperatedrosseln.Local.Controllers.MainController;
import Desperatedrosseln.Local.Controllers.LobbyController;

import Desperatedrosseln.Local.Controllers.ScreenSizeErrorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application implements Runnable {

    private final int minSupportedWidth = 1024;
    private final int minSupportedHeight = 768;

    // sceneNumber 0: mainScene, 1: loginScene, 2: lobbyScene, 3: screenSizeErrorScene
    @Override
    public void start(Stage primaryStage) {

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double userScreenHeight = screenBounds.getHeight();
        double userScreenWidth = screenBounds.getHeight();

        try {
            if (userScreenHeight < minSupportedHeight || userScreenWidth < minSupportedWidth) {
                launchScene(primaryStage, 3);
            } else {
                launchScene(primaryStage, 1);
            }
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
        } else if (sceneNumber == 3) {
            ScreenSizeErrorController screenSizeErrorController = new ScreenSizeErrorController();
            screenSizeErrorController.startScene(stage);
        }
    }
    @Override
    public void run(){launch();}

}
