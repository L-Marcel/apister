package app.controllers;

import java.io.IOException;

import app.App;
import javafx.fxml.FXML;

public class Secondary {
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    };
};