package Desperatedrosseln.Local.Protocols;

public class SetStatus {

    /*
    Body: "ready": true
     */
    private boolean ready;

    public boolean isReady() {
        return ready;
    }

    public SetStatus (boolean ready){
        this.ready = ready;
    }
}
