package Desperatedrosseln.Local.Protocols;

public class CardSelected {

    /*
    Body:
        "clientID": 42,
        "register": 5,
        "filled": true
     */
    private Integer clientID;
    private Integer register;

    private boolean filled;
    public CardSelected (int clientID, int register, boolean filled){
        this.clientID = clientID;
        this.register = register;
        this.filled = filled;
    }

    public Integer getClientID() {
        return clientID;
    }

    public Integer getRegister() {
        return register;
    }
    public boolean isFilled() {
        return filled;
    }

}
