package app.controllers.components;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConfirmDialogController extends DialogController<Boolean> {
    private String warning;

    @FXML private Label warningLabel;
    @FXML private Button cancelButton;

    public ConfirmDialogController(String title, String warning) {
        super(title, false, false);
        this.warning = warning;
    };

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        this.warningLabel.setText(this.warning);
        super.initialize(url, resource);
    };

    @FXML
    public void confirm() {
        this.close(true);
    };

    @Override
    public void cancel() {
        this.close(false);
    };

    @Override
    public void focus() {
        this.cancelButton.requestFocus();
    };
};
