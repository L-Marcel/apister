package app.layout;

import java.io.IOException;

import app.controllers.components.ConfirmDialogController;

public class ConfirmDialog extends Dialog<Boolean> {
    public ConfirmDialog(String title, String warning) throws IOException {
        super(
            title,
            "components/confirmDialog",
            new ConfirmDialogController(title, warning)
        );
    };
};
