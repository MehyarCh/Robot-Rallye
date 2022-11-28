package client.desperatedrosselnhp;

import server.connection.ClientHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import server.connection.Server;

import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class LoginController {

    @FXML
    private DatePicker birthdate;
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginTextField;
    private Date birth;
    private GameApplication gameApp;
    private ClientHandler handler;
    private boolean available = true;
    private ArrayList<Client> clients;



    public LoginController() throws IOException {
        Socket socket = new Socket("localhost", 1337);
        handler = new ClientHandler(socket);
    }


    @FXML
    void onLogin(ActionEvent event) throws IOException, InterruptedException {
        try {

            String msg = loginTextField.getText();
            setClients(ClientHandler.clients);
            int i=0;
            System.out.println(clients == null);
            //check if username
            while (clients != null && i< clients.size()) {
                if(clients.get(i).getName().equals(msg.toLowerCase())){
                    available = false;
                    System.out.println("available: " + available);
                    break;
                }
                i++;
            }
            if(available){
                //start chat window, transfer username to chatController
                gameApp = new ChatApplication();
                gameApp.setUserName(msg);
                gameApp.setDates(birth, date);
                startChat();
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            }else {
                //Error dialog if username not available
                System.out.println("available fel else: " + available);
                Dialog<String> dialog = new Dialog<String>();
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                dialog.setContentText("Username not available");
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.show();
            }

        } catch(IOException E) {
            E.printStackTrace();
        }
    }


    public void onBirthdate(){
        LocalDate localDate = birthdate.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        birth = Date.from(instant);
        System.out.println(birth.toString());
    }

    private void startUI(){

    }

    private boolean checkAvailable(String username){
       return true;
    }

    public void setClients(ArrayList<Client> clients){
        this.clients = clients;
    }

}
