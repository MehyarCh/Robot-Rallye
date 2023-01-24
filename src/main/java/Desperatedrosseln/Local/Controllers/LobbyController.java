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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static Desperatedrosseln.Local.Controllers.LoginController.client;

public class LobbyController {
    private Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String selectedMap;

    @FXML
    private VBox center;

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

    private static final Logger logger = LogManager.getLogger(LobbyController.class);

    private List<Button> robotIcons = new ArrayList<>();

    private List<Button> selectedIcons = new ArrayList<>();

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

    /**
     * starts the lobby scene
     * already disables all unavailable robot-choices
     */
    public void startLobbyScene(Stage stage) {
        mainController = new MainController();
        client.setMainController(mainController);
        client.setLobbyController(this);
        client.setLobbyControllerInitialized(true);
        client.sendHelloServer();
        this.stage = stage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        //playersonline.setText("Players currently in lobby: " );
        initRobotIconsList();
        //center.requestFocus();
        stage.show();
        disableTakenRobots(client.getRobotIDs());
    }

    /**
     * initializes a list containing all robot-choice-buttons
     */
    private void initRobotIconsList(){
        robotIcons.add(playerIcon1);
        robotIcons.add(playerIcon2);
        robotIcons.add(playerIcon3);
        robotIcons.add(playerIcon4);
        robotIcons.add(playerIcon5);
        robotIcons.add(playerIcon6);
    }

    /**
     * this method is called when the user chooses one of the robots in the GUI (icons)
     * sets the clients selectedrobot to the chosen one
     * disables all other robot choices for this player
     * sends PlayerValues protocoll message to the Server
     */
    @FXML
    public void onButtonClicked(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        int selectedRobot = 0;
        switch (clickedButton.getId()) {
            case "player-icon--1":
                logger.info(client.getName() + " chose BROWN");
                selectedRobot = 1;
                break;
            case "player-icon--2":
                logger.info(client.getName() + " chose YELLOW");
                selectedRobot = 2;
                break;
            case "player-icon--3":
                logger.info(client.getName() + " chose BLUE");
                selectedRobot = 3;
                break;
            case "player-icon--4":
                logger.info(client.getName() + " chose GREEN");
                selectedRobot = 4;
                break;
            case "player-icon--5":
                logger.info(client.getName() + " chose ORANGE");
                selectedRobot = 5;
                break;
            case "player-icon--6":
                logger.info(client.getName() + " chose RED");
                selectedRobot = 6;
                break;
        }

        if(!client.getRobotIDs().contains(selectedRobot)){
            selectedIcons.add(clickedButton);

            this.selectedRobot = selectedRobot;
            mainController.setSelectedRobot(selectedRobot);
            readyButton.setDisable(false);
            for (Button button: robotIcons) {

                if (event.getSource() != button) {
                    button.setDisable(true);
                }
            }
        }
        client.sendPlayerValues(selectedRobot);

    }

    /**
     * this method gets called when the user sends a text message with the ENTER key on the keyboard
     */
    @FXML
    public void onMessageSend(KeyEvent event){
        if (event.getCode().toString().equals("ENTER")) {
            onClickSend();
        }
    }

    /**
     * this method gets called when the user sends a text message with the send button in the GUI
     */
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

    /**
     * @param robotIDs contains all the already taken robots
     * this method disables every robot icon in the GUI that has already been chosen by other players
     */
    @FXML
    public void disableTakenRobots(ArrayList<Integer> robotIDs){
        if (robotIDs.contains(1)){
            playerIcon1.setDisable(true);
        }
        if (robotIDs.contains(2)){
            playerIcon2.setDisable(true);
        }
        if (robotIDs.contains(3)){
            playerIcon3.setDisable(true);
        }
        if (robotIDs.contains(4)){
            playerIcon4.setDisable(true);
        }
        if (robotIDs.contains(5)){
            playerIcon5.setDisable(true);
        }
        if (robotIDs.contains(6)){
            playerIcon6.setDisable(true);
        }
    }

    /**
     * @param figure is the chosen robot whose icon-button should be disabled
     * this method is used from outside of this class to disable only one robot-icon
     */
    public void disableRobotIcon(int figure){
        switch (figure){
            case 1:
                playerIcon1.setDisable(true);
                break;
            case 2:
                playerIcon2.setDisable(true);
                break;
            case 3:
                playerIcon3.setDisable(true);
                break;
            case 4:
                playerIcon4.setDisable(true);
                break;
            case 5:
                playerIcon5.setDisable(true);
                break;
            case 6:
                playerIcon6.setDisable(true);
                break;
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
        JsonAdapter<MapSelected> mapSelectedJsonAdapter = moshi.adapter(MapSelected.class);
        client.sendMessage("MapSelected", mapSelectedJsonAdapter.toJson(new MapSelected(selectedMap.replaceAll("\s",""))));
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

        Image image = switch (mapName) {
            case "DizzyHighway" -> new Image(getClass().getResource("/images/maps/dizzyHighway.png").toString());
            case "ExtraCrispy" -> new Image(getClass().getResource("/images/maps/extraCrispy.png").toString());
            case "LostBearings" -> new Image(getClass().getResource("/images/maps/lostBearings.png").toString());
            case "DeathTrap" -> new Image(getClass().getResource("/images/maps/deathTrap.png").toString());
            case "Twister" -> new Image(getClass().getResource("/images/maps/twister.png").toString());
            default -> new Image(getClass().getResource("/images/maps/no_such_card.png").toString());
        };

        ImageView mapOptionImage = new ImageView(image);
        mapOptionImage.setPreserveRatio(true);
        mapOptionImage.setFitHeight(62);
        mapOptionImage.getStyleClass().add("map-option-image");

        StackPane mapOption = new StackPane(mapOptionImage, mapOptionOverlay);
        mapOption.getStyleClass().add("map-option");
        mapOption.getStyleClass().add("border-radius");
        mapOption.getStyleClass().add("border-radius--sm");

        mapOption.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            selectedMap = mapName;
            System.out.println("#########++++++++++###########" + selectedMap);
        });

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

