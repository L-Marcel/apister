package app.controllers.components;

import java.net.URL;
import java.util.ResourceBundle;

import app.layout.TableCellTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class TableCellTextFieldController implements Initializable{
    private TableCellTextField cell;
    
    @FXML private TextField textField;  

    public TableCellTextFieldController(TableCellTextField cell) {
        this.cell = cell;
    }

    @FXML
    public void initialize(URL arg0, ResourceBundle arg1) {
        textField.setOnAction(event -> this.commitEdit());
        textField.setText(cell.getItem());
        textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused && cell.getItem() != textField.getText()) this.commitEdit();
        });
    }

    private void commitEdit() {
        cell.callback.call(cell.getIndex(), textField.getText());
    }
};
