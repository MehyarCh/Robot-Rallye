package Desperatedrosseln.Local.Protocols;

public class ReceivedChat {

    /*
    Body:
         "message": "Yoh, Bob! How is your head doing after last night?",
         "from": 42,
         "isPrivate": true (bei DirektNachricht, false bei Nachricht an alle)
     */

    private String message;
    private int from;
    private boolean isPrivate;

    public String getMessage() {
        return message;
    }

    public int getFrom() {
        return from;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public ReceivedChat(String message, int from, boolean isPrivate) {
        this.message = message;
        this.from = from;
        this.isPrivate = isPrivate;
    }
}
