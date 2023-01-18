package Desperatedrosseln.Local.Controllers;

import Desperatedrosseln.Local.Protocols.MapSelected;
import Desperatedrosseln.Local.Protocols.Message;
import Desperatedrosseln.Local.Protocols.SetStatus;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.application.Platform;
import Desperatedrosseln.Local.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static Desperatedrosseln.Local.Controllers.LoginController.client;

public class LobbyController {
    private Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
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
    @FXML
    private ToggleButton readyButton;
    @FXML
    private TextField chat_input_lobby;
    @FXML
    private TextFlow chatlog_lobby;
    @FXML
    private ChoiceBox<String> mapSelection;
    @FXML
    private HBox mapOptionWrapper;
    @FXML
    private Button validateMapChoice;
    public MainController mainController;
    private boolean ready = false;
    @FXML
    private Button playerIconPink;
    @FXML
    private Label playersonline;
    private int selectedRobot;

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

    public Stage getStage() {
        return stage;
    }

    public void startLobbyScene(Stage stage) {
        mainController = new MainController();
        client.setMainController(mainController);
        client.setLobbyController(this);
        client.sendHelloServer();
        this.stage = stage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        //playersonline.setText("Players currently in lobby: " );
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
            this.selectedRobot = selectedRobot;
            mainController.setSelectedRobot(selectedRobot);
            readyButton.setDisable(false);
        }
        client.sendPlayerValues(selectedRobot);

    }

    @FXML
    public void onMessageSend(KeyEvent event){
        if (event.getCode().toString().equals("ENTER")) {
            onClickSend();
        }
    }
    @FXML
    public void onClickSend() {
        if (!chat_input_lobby.getText().isEmpty()) {
            String msg = chat_input_lobby.getText();

            //System.out.println(client.getName()+ ": "+ msg);

            client.sendChatMessage(msg, -1);

            chat_input_lobby.setText("");
            chat_input_lobby.requestFocus();
        }
    }
    @FXML
    public void addChatMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatlog_lobby.getChildren().add(new Text(message + "\n"));
            }
        });
    }
    @FXML
    public void onChooseMap(){
        String map = mapSelection.getValue();
        JsonAdapter<MapSelected> mapSelectedJsonAdapter = moshi.adapter(MapSelected.class);
        client.sendMessage("MapSelected", mapSelectedJsonAdapter.toJson(new MapSelected(map.replaceAll("\s",""))));
        validateMapChoice.setDisable(true);
    }
    @FXML
    public void onReady() {
        JsonAdapter<SetStatus> setStatusJsonAdapter = moshi.adapter(SetStatus.class);
        client.sendMessage("SetStatus", setStatusJsonAdapter.toJson(new SetStatus(readyButton.isSelected())));
    }

    public void addMapsToChoice(List<String> maps){

        Platform.runLater(() -> {
            for(String mapName: maps){
                System.out.println(mapName);

                StackPane mapOption = createMapOption(mapName);
                mapOptionWrapper.getChildren().add(mapOption);
            }
        });

    }

    private StackPane createMapOption(String mapName) {
        Label mapOptionTitle = new Label(mapName);
        mapOptionTitle.getStyleClass().add("map-option-title");
        mapOptionTitle.getStyleClass().add("color--light");

        HBox mapOptionOverlay = new HBox(mapOptionTitle);
        mapOptionOverlay.getStyleClass().add("map-option-overlay");

        System.out.println("########################################### Going In");

        Image image;
        switch (mapName) {
            case "DizzyHighway":
                image = new Image(getClass().getResource("/images/Maps/dizzyHighway.png").toString());
                break;
            case "ExtraCrispy":
                image = new Image(getClass().getResource("/images/Maps/dizzyHighway.png").toString());
            case "LostBearings":
                image = new Image(getClass().getResource("/images/Maps/dizzyHighway.png").toString());
            case "DeathTrap":
                image = new Image(getClass().getResource("/images/Maps/dizzyHighway.png").toString());
            default:
                image = new Image(getClass().getResource("/images/Maps/dizzyHighway.png").toString());
                break;
        }
        ImageView mapOptionImage = new ImageView(image);
        mapOptionImage.setPreserveRatio(true);
        mapOptionImage.setFitHeight(62);
        mapOptionImage.getStyleClass().add("map-option-image");

        StackPane mapOption = new StackPane(mapOptionImage, mapOptionOverlay);
        mapOption.getStyleClass().add("map-option");
        mapOption.getStyleClass().add("border-radius");
        mapOption.getStyleClass().add("border-radius--sm");

        return mapOption;
    }

    public void canChooseMap() {
        //mapSelection.setDisable(false);
        validateMapChoice.setDisable(false);
    }
    public int getSelectedRobot(){
        return this.selectedRobot;
    }

}

