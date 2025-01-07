package app.layout;

import java.io.IOException;

import app.App;
import app.controllers.components.ProjectCellController;
import app.log.Log;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

public class ProjectCell extends ListCell<String> {
    @Override
    protected void updateItem(String project, boolean empty) {
        super.updateItem(project, empty);

        this.setPadding(new Insets(0));
        this.getStyleClass().clear();

        if(empty || project == null) {
            this.setText(null);
            this.setGraphic(null);
            this.getStyleClass().add("project-cell");
        } else {
            this.setText(null);
            try {
                ProjectCellController projectCellController = new ProjectCellController(this);
                Parent cell = App.load(
                    "components/projectCell", 
                    projectCellController
                );

                this.setGraphic(cell);

                if(this.getIndex() % 2 == 1) {
                    this.getStyleClass().add("project-cell-alt");
                } else {
                    this.getStyleClass().add("project-cell-main");
                };
            } catch(IOException e) {
                this.setGraphic(null);
                this.getStyleClass().add("project-cell");
                Log.print("Can't load project cell.");
                Log.print("Error", e.getMessage());
            };
        };
    };
};
