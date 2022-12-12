package Desperatedrosseln.Local.Protocols;

public class SendChat {

    /*
    Body:
           "message": "Yoh, Bob! How is your head doing after last night?",
            "to": 42 (für Nachricht an alle: -1)
     */

    private String message;
    private int to;

    public String getMessage() {
        return message;
    }

    public int getTo() {
        return to;
    }

    public SendChat(String message, int to) {
        this.message = message;
        this.to = to;
    }
}
