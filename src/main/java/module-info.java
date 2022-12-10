module desperatedrosseln {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens Desperatedrosseln to javafx.fxml;
    opens Desperatedrosseln.Local.Controllers to javafx.fxml;
    exports Desperatedrosseln;
    exports Desperatedrosseln.Local.Controllers to javafx.fxml;

}