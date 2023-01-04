package Desperatedrosseln.Local.Protocols;

public class HelloServer {

   /*
   Body:
          "group": "TolleTrolle",
          "isAI": false,
          "protocol": "Version 0.1"

    */
    private String group;
    public final boolean isAI;
    private String protocol;

    public String getGroup() {
        return group;
    }


    public String getProtocol() {
        return protocol;
    }

    public HelloServer(String group, boolean isAI, String protocol){
        this.group = group;
        this.isAI = isAI;
        this.protocol = protocol;
    }
}
