package app.controllers.components;

import java.net.URL;
import java.util.ResourceBundle;

import app.core.Projects;
import app.errors.InvalidInput;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AddDialogProjectController extends DialogController<String> implements Initializable {
    @FXML private TextField textField;
    @FXML private Label errorLabel;

    @FXML
    public void clearError() {
        this.errorLabel.setText("Nome disponível!");
        this.errorLabel.getStyleClass().clear();
        this.errorLabel.getStyleClass().add("dialog-success");
    };

    private void showError(String message) {
        this.errorLabel.setText(message);
        this.errorLabel.getStyleClass().clear();
        this.errorLabel.getStyleClass().add("dialog-error");
        this.textField.requestFocus();
    };

    public void setTextField (String text) {
        this.textField.setText(text);
    }
    
    @FXML
    public void confirm() {
        String candidate = this.textField.getText();
        candidate = candidate.trim();

        try {
            Projects.validate(candidate);
            this.close(candidate);
        } catch (InvalidInput e) {
            showError(e.getMessage());
        };
    };

    @Override
    public void cancel() {
        this.close();
    };

    @Override
    public void focus() {
        this.textField.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        textField.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if(event.getCode() == KeyCode.ENTER) {
                this.confirm();
            };
        });

        textField.addEventHandler(KeyEvent.KEY_TYPED, (event) -> {
            String candidate = this.textField.getText() + event.getCharacter();
            candidate = candidate.trim();
            try {
                Projects.validate(candidate);
                clearError();
            } catch (InvalidInput e) {
                showError(e.getMessage());
            };
        });
    };
};
