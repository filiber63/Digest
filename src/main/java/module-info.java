module com.example.digest {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.digest to javafx.fxml;
    exports com.digest;
}