package app.layout;

import java.io.IOException;

import app.App;
import app.controllers.components.TableCellTextFieldController;
import app.core.HeaderEntry;
import app.interfaces.TableCellTextFieldEditCallback;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;

public class TableCellTextField extends TableCell<HeaderEntry, String> {
    public TableCellTextFieldEditCallback callback;

    public TableCellTextField(TableCellTextFieldEditCallback callback) {
        this.callback = callback;
    };

    @Override
    public void updateItem(String value, boolean empty) {
        super.updateItem(value, empty);

        this.setPadding(new Insets(0));
        this.getStyleClass().clear();
        this.getStyleClass().add("table-cell");

        if(empty) {
            this.setGraphic(null);
        } else {
            try {
                TableCellTextFieldController cellController = new TableCellTextFieldController(this);
                Parent cell = App.load("components/tableCellTextField", cellController);
                this.setGraphic(cell);
            } catch(IOException e) {
                this.setGraphic(null);
                e.printStackTrace();
            };
        };
    };
};
