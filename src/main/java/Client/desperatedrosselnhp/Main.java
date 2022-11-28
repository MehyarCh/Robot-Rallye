package client.desperatedrosselnhp;

public class Main {
    public static void main(String[] args){
        LoginApplication app = new LoginApplication();
        new Thread(app).start();
    }
}
