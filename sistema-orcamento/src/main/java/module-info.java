module com.grafica {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.grafica to javafx.fxml;
    exports com.grafica;
}
