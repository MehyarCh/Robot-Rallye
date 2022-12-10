package Desperatedrosseln.Local.Protocols;

public class Message {
    private String name;
    private String body;
    private MessageType messageType;
    private String sender;


    public Message(String name, MessageType type, String body){
        this.body = body;
        this.name = name;
        this.messageType = type;
    }

    public String getSender() {
        return sender;
    }

    public MessageType getType() {
        return messageType;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }
}


