package app.layout;

import java.io.IOException;
import java.util.Map;

import app.App;
import app.controllers.components.TableCellTextFieldController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;

public class TableCellTextField extends TableCell<Map.Entry<String, String>, Integer> {
    @Override
    public void updateItem(Integer value, boolean empty) {
        super.updateItem(value, empty);

        this.setPadding(new Insets(0));
        this.getStyleClass().clear();
        
        if(empty) {
            this.setText(null);
            this.setGraphic(null);
            this.getStyleClass().add("table-cell");
        } else {
            this.setText(null);

            try {
                TableCellTextFieldController cellController = new TableCellTextFieldController(this);
                Parent cell = App.load("components/tableCellTextField", cellController);

                this.setGraphic(cell);
                if(this.getIndex() % 2 == 1) {
                    this.getStyleClass().add("table-cell-alt");
                } else {
                    this.getStyleClass().add("table-cell-main");
                };
            } catch(IOException e) {
                this.setGraphic(null);
                this.getStyleClass().add("table-cell");
                e.printStackTrace();
            };
        };
    };
};
