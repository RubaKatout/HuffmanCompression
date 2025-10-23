module com.example.algoass2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;


    opens com.example.algoass2 to javafx.fxml;
    exports com.example;
}