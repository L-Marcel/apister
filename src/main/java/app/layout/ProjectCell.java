package app.layout;

import java.io.IOException;

import app.App;
import app.controllers.ProjectCellController;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

public class ProjectCell extends ListCell<String> {
    @Override
    protected void updateItem(String project, boolean empty) {
        super.updateItem(project, empty);
        if (empty || project == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            try {
                ProjectCellController projectCellController = new ProjectCellController(this);
                Parent cell = App.load("projectCell", projectCellController);
                setGraphic(cell);
            } catch (IOException e) {
                setGraphic(null);
            };
        };
    };
};
