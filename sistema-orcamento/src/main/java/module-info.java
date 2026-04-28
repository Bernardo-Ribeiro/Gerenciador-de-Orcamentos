module com.grafica {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.grafica to javafx.fxml;
    opens com.grafica.controller to javafx.fxml;
    exports com.grafica;
}
