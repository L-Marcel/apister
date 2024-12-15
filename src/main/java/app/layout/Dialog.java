package app.layout;

import java.io.IOException;

import app.App;
import app.controllers.components.DialogController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Dialog<R> extends Stage {
    private R result;

    public Dialog(
        String title, 
        String file, 
        DialogController<R> controller
    ) throws IOException {
        Parent parent = App.load(file, controller);
        App.applyCss(parent, file);
        controller.setDialog(this);
        this.setScene(new Scene(parent));
        this.initOwner(App.getScene().getWindow());
        this.initStyle(StageStyle.DECORATED);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setAlwaysOnTop(true);
        this.setResizable(false);
        this.setTitle(title);
    };

    public R getResult() {
        return this.result;
    };

    public void setResult(R result) {
        this.result = result;
    };

    public R showAndGet() {
        this.showAndWait();
        return this.result;
    };
};
