module com.example.rgtonfa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;

    opens com.example.rgtonfa to javafx.fxml;
    exports com.example.rgtonfa;
}