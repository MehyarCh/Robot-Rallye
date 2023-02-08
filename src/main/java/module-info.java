module desperatedrosseln {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires moshi;
    requires com.google.gson;
    requires org.apache.logging.log4j;
    requires java.desktop;

    opens Desperatedrosseln to javafx.fxml;
    opens Desperatedrosseln.Local.Controllers to javafx.fxml;
    opens Desperatedrosseln.Local.Protocols to moshi, com.google.gson;
    opens Desperatedrosseln.Logic.Elements.Tiles to moshi, com.google.gson;
    opens Desperatedrosseln.Json.utils to com.google.gson;
    opens Desperatedrosseln.Logic.Cards to moshi;
    opens Desperatedrosseln.Logic.Elements to com.google.gson, moshi;
    exports Desperatedrosseln;
}