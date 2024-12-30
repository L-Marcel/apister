package app.controllers.components;

import java.net.URL;
import java.util.ResourceBundle;

import app.layout.Dialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public abstract class DialogController<R> implements Initializable {
    private String title;
    private Dialog<R> dialog;
    private R defaultValue;
    private R closeValue;

    @FXML
    private Label titleLabel;

    public DialogController(String title, R defaultValue, R closeValue) {
        this.title = title;
        this.defaultValue = defaultValue;
        this.closeValue = closeValue;
    };

    public abstract void cancel();
    public abstract void confirm();
    public abstract void focus();

    protected void close() {
        this.dialog.setResult(closeValue);
        this.dialog.close();
    };

    protected void close(R result) {
        this.dialog.setResult(result);
        this.dialog.close();
    };

    public void setDialog(Dialog<R> dialog) {
        this.dialog = dialog;
        this.dialog.setResult(defaultValue);
    };

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        this.titleLabel.setText(this.title);
        Platform.runLater(() -> this.focus());
    };
};
