module desperatedrosseln {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires moshi;

    opens Desperatedrosseln to javafx.fxml;
    opens Desperatedrosseln.Local.Controllers to javafx.fxml;
    opens Desperatedrosseln.Local.Protocols to moshi;
    opens Desperatedrosseln.Logic.Elements to moshi;
    exports Desperatedrosseln;

}