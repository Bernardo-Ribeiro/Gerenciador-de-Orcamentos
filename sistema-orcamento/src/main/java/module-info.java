module com.grafica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.grafica to javafx.fxml;
    opens com.grafica.controller to javafx.fxml;
    opens com.grafica.model to javafx.fxml;
    exports com.grafica;
    exports com.grafica.model;
    exports com.grafica.dao;
    exports com.grafica.service;
    exports com.grafica.controller;
}
