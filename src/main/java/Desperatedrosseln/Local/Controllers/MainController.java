package Desperatedrosseln.Local.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public MainController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainScene.fxml"));
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

    public void startMainScene(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        stage.setMinHeight(720);
        stage.setMinWidth(1280);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
    }
}
