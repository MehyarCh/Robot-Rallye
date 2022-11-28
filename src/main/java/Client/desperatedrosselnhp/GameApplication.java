package client.desperatedrosselnhp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("roborally.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1133, 744);
        stage.setTitle("Board");
        stage.setScene(scene);
        stage.show();
    }
}
