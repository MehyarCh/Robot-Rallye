package Desperatedrosseln.Local.Protocols;

public class Error {

    // Body: "error": "Whoops. That did not work. Try to adjust something."
    private String error;

    public String getError() {
        return error;
    }

    public Error(String error){
        this.error = error;
    }
    public Error(){
        this.error = "Whoops. That did not work. Try to adjust something.";
    }
}
