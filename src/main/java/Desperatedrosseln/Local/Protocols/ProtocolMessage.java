package Desperatedrosseln.Local.Protocols;


public class ProtocolMessage<T> {
    private String messageType;
    private T messageBody;

    public ProtocolMessage(String messageType, T messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public String getMessageType() {
        return messageType;
    }

    public T getMessageBody() {
        return messageBody;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setMessageBody(T messageBody) {
        this.messageBody = messageBody;
    }
}
