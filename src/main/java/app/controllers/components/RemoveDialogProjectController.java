package app.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RemoveDialogProjectController extends DialogController<Boolean> {
    @FXML private Button cancelButton;

    public RemoveDialogProjectController() {
        super(false, false);
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
