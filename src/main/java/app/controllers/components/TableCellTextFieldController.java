package app.controllers.components;

import app.layout.TableCellTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TableCellTextFieldController {
    private TableCellTextField tableCellTextField;
    
    @FXML private TextField textField;  

    public TableCellTextFieldController(TableCellTextField tableCellTextField) {
        this.tableCellTextField = tableCellTextField;
    }
};
