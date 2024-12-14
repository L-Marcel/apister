package app.controllers;

import app.core.Projects;
import app.errors.InvalidInput;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddProjectController {
    private Dialog<String> addDialog;

    @FXML private TextField textField;
    @FXML private Label errorLabel;

    public AddProjectController(Dialog<String> addDialog) {
        this.addDialog = addDialog;
    };

    @FXML
    public void clearError() {
        textField.setVisible(false);
    };

    @FXML
    public void close() {
        this.addDialog.setResult("");
        this.addDialog.close();
    };
    
    @FXML
    public void confirm() {
        String candidate = textField.getText();

        try {
            Projects.validate(candidate);
            this.addDialog.setResult(candidate);
            this.addDialog.close();
        } catch (InvalidInput e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
            textField.requestFocus();
        };
    }
};
