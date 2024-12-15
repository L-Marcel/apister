package app.controllers.components;

import app.layout.Dialog;
import javafx.scene.Parent;

public abstract class DialogController<R> {
    public Parent focus;
    private Dialog<R> dialog;

    public abstract void focus();

    protected void close() {
        this.dialog.setResult(null);
        this.dialog.close();
    };

    protected void close(R result) {
        this.dialog.setResult(result);
        this.dialog.close();
    };

    public void setDialog(Dialog<R> dialog) {
        this.dialog = dialog;
    };

    public abstract void cancel();
    public abstract void confirm();
};
