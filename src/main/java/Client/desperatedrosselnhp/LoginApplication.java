package client.desperatedrosselnhp;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;

/**
 * Represents the login window
 * @author
 */
public class LoginApplication extends Application implements Runnable {
    @Override
    public void start(Stage stage) throws IOException {
        //Login window
        FXMLLoader login = new FXMLLoader(LoginApplication.class.getResource("loginbox.fxml"));
        Scene scene2 = new Scene(login.load(), 225, 200);
        stage.setTitle("RoboRallye");
        stage.setScene(scene2);
        stage.show();
    }

    @Override
    public void run(){
        launch();
    }
}

