package app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import app.controllers.components.AddDialogProjectController;
import app.core.Project;
import app.core.Projects;
import app.layout.Dialog;
import app.layout.ProjectCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ProjectsController implements Initializable {
    @FXML private Button addButton;
    @FXML private ListView<String> listView;

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		Projects projects = Projects.getInstance();
        this.listView.setItems(projects.get());
        this.listView.setCellFactory((list) -> new ProjectCell());
	};

    @FXML
    public void addProject() {
        try {
            Dialog<String> dialog = new Dialog<String>(
                null,
                "components/addProjectDialog",
                new AddDialogProjectController()
            );

            String name = dialog.showAndGet();
            if(name != null) {
                new Project(name);
                Projects.getInstance().add(name);
            };
        } catch (IOException exception) {
            exception.printStackTrace();
        };
    };
};
