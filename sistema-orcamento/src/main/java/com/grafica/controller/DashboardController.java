package com.grafica.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {

    @FXML
    private void initialize() {
    }

    // Agregamos el método fantasma para que el FXML lo encuentre
    @FXML
    private void selecionarGestaoUsuarios(ActionEvent event) {
        System.out.println("Botón de Gestión de Usuarios presionado");
    }
}