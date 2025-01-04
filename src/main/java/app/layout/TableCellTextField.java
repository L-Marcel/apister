package app.layout;

import java.io.IOException;

import app.App;
import app.controllers.components.TableCellTextFieldController;
import app.interfaces.TableCellTextFieldEditCallback;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.util.Pair;

public class TableCellTextField extends TableCell<Pair<String, String>, String> {
    public TableCellTextFieldEditCallback callback;

    public TableCellTextField(TableCellTextFieldEditCallback callback) {
        this.callback = callback;
    }

    @Override
    public void updateItem(String value, boolean empty) {
        super.updateItem(value, empty);

        this.setPadding(new Insets(0));
        this.getStyleClass().clear();

        if(empty) {
            this.setGraphic(null);
            this.getStyleClass().add("table-cell");
        } else {
            try {
                TableCellTextFieldController cellController = new TableCellTextFieldController(this);
                Parent cell = App.load("components/tableCellTextField", cellController);

                this.setGraphic(cell);

                if(this.getIndex() % 2 == 1) {
                    this.getStyleClass().add("table-cell-alt");
                } else {
                    this.getStyleClass().add("table-cell-main");
                };
            } catch (IOException e) {
                this.setGraphic(null);
                this.getStyleClass().add("table-cell");
                e.printStackTrace();
            };
        };
    };
};
