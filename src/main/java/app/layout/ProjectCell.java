package app.layout;

import java.io.IOException;

import app.App;
import app.controllers.ProjectCellController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

public class ProjectCell extends ListCell<String> {
    @Override
    protected void updateItem(String project, boolean empty) {
        super.updateItem(project, empty);

        setPadding(new Insets(0));
        getStyleClass().add("project-cell");
        if(getIndex() % 2 == 1) {
            getStyleClass().add("project-cell-alt");
        };

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
