package app.controllers.components;

import java.net.URL;
import java.util.ResourceBundle;

import app.errors.InvalidInput;
import app.interfaces.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextFieldDialogController extends DialogController<String> {
    private String prompt;
    private String validMessage;
    private String initial;
    private Validator<String> validator;
    private Boolean trim;

    @FXML private TextField textField;
    @FXML private Label errorLabel;

    public TextFieldDialogController(
        String title,
        String prompt, 
        String validMessage, 
        String initial,
        Validator<String> validator,
        Boolean trim
    ) {
        super(title, null, null);
        this.prompt = prompt;
        this.validMessage = validMessage;
        this.initial = initial;
        this.validator = validator;
        this.trim = trim;
        if(trim) this.initial = this.initial.trim();
    };

    @FXML
    public void clearError() {
        this.errorLabel.setText(this.validMessage);
        this.errorLabel.getStyleClass().clear();
        this.errorLabel.getStyleClass().add("dialog-success");
    };

    private void showError(String message) {
        this.errorLabel.setText(message);
        this.errorLabel.getStyleClass().clear();
        this.errorLabel.getStyleClass().add("dialog-error");
        this.textField.requestFocus();
    };

    public boolean validate(String candidate) {
        try {
            if(trim) candidate = candidate.trim();
            if(validator != null) validator.validate(candidate);
            clearError();
            return true;
        } catch(InvalidInput e) {
            showError(e.getMessage());
        };

        return false;
    };
    
    @FXML
    public void confirm() {
        String candidate = this.textField.getText();
        if(this.validate(candidate)) this.close(candidate);
    };

    @Override
    public void cancel() {
        this.close();
    };

    @Override
    public void focus() {
        this.textField.requestFocus();
    };

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        this.textField.setPromptText(this.prompt);
        this.textField.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if(event.getCode() == KeyCode.ENTER) this.confirm();
        });

        this.textField.addEventHandler(KeyEvent.KEY_TYPED, (event) -> {
            this.validate(this.textField.getText() + event.getCharacter());
        });
        
        this.textField.setText(this.initial);
        this.validate(this.initial);

        super.initialize(url, resource);
    };
};
