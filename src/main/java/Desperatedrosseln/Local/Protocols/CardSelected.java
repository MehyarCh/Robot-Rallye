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


    public Integer getClientID() {
        return clientID;
    }

    public Integer getRegister() {
        return register;
    }

    public CardSelected (int clientID, int register){
        this.clientID = clientID;
        this.register = register;
    }
}
