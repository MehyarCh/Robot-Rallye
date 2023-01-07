module desperatedrosseln {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires moshi;
    requires com.google.gson;

    opens Desperatedrosseln to javafx.fxml;
    opens Desperatedrosseln.Local.Controllers to javafx.fxml;
    opens Desperatedrosseln.Local.Protocols to moshi, com.google.gson;
    opens Desperatedrosseln.Logic.Elements.tiles to moshi, com.google.gson;
    opens Desperatedrosseln.Json.utils to com.google.gson;
    exports Desperatedrosseln;

}