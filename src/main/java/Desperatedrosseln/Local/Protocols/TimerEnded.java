package Desperatedrosseln.Local.Protocols;

import java.util.ArrayList;

public class TimerEnded {

    /*
    Body:
        "clientIDs": [
            1,
            3,
            6
        ]
     */
    private ArrayList<Integer> clientIDs;

    public ArrayList<Integer> getClientIDs() {
        return clientIDs;
    }

    public TimerEnded(ArrayList<Integer> clientIDs) {
        this.clientIDs = clientIDs;
    }
}
