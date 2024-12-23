package app.controllers.components;

import app.core.Projects;
import app.errors.InvalidInput;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class RemoveDialogProjectController extends DialogController<Boolean> implements Initializable {

    
    @FXML
    public void confirm() {
        this.close(true);
    }

    @Override
    public void cancel() {
        this.close(false);
    };

    @Override
    public void focus() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
    };
};
