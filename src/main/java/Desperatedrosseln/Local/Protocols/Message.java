package Desperatedrosseln.Local.Protocols;

public class Message {
    private String messageType;
    private String messageBody;

    public Message(String type, String messageBody) {
        this.messageType = type;
        this.messageBody = messageBody;
    }
    public String getMessageType() {
        return messageType;
    }
    public String getMessageBody() {
        return messageBody;
    }

}


