package app.controllers.components;

import java.net.URL;
import java.util.ResourceBundle;

import app.layout.TableCellTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class TableCellTextFieldController implements Initializable {
    private TableCellTextField cell;
    
    @FXML private TextField textField;  

    public TableCellTextFieldController(TableCellTextField cell) {
        this.cell = cell;
    };

    @FXML
    public void initialize(URL url, ResourceBundle resource) {
        this.textField.setOnAction(event -> this.commitEdit());
        this.textField.setText(this.cell.getItem());
        this.textField.focusedProperty().addListener(
            (obs, wasFocused, isFocused) -> {
                if(!isFocused && this.cell.getItem() != this.textField.getText()) 
                    this.commitEdit();
            }
        );
    };

    private void commitEdit() {
        this.cell.callback.call(
            this.cell.getTableRow().getItem(), 
            this.textField.getText()
        );
    };
};
