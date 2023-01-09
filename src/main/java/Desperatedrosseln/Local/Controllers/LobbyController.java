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

public class LobbyController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public MainController mainController;

    private Client client;

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
    public void switchToMainScene(ActionEvent event) throws IOException {
        mainController = new MainController();
        System.out.println("mainctrl null= " + mainController==null);
        mainController.startMainScene(stage);
    }
    @FXML
    public void onPlayerSix() throws IOException {

        System.out.println("mainctrl null= " + mainController==null);
        if(mainController == null){
            mainController = new MainController();
            client.setMainController(mainController);
        }
        mainController.startMainScene(stage);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public TextFlow getTextFlow() {
        return textFlow;
    }
}
