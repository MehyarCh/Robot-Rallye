package Desperatedrosseln;

public class AppStart {
    public static void main(String[] args){
        Main mainApp = new Main();
        Thread thread = new Thread(mainApp);
        thread.start();
    }

}
