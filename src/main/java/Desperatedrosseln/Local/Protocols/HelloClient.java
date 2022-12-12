package Desperatedrosseln.Local.Protocols;

public class HelloClient {

    // Body: "protocol": "Version 0.1"
    public String protocol;

    public String getProtocol() {
        return protocol;
    }

    public HelloClient(String protocol){
        this.protocol = protocol;
    }
}
