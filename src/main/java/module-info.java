module client.desperatedrosselnhp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens client.desperatedrosselnhp to javafx.fxml;
    exports client.desperatedrosselnhp;
}