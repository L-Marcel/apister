package app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import app.App;
import app.core.Project;
import app.core.Projects;
import app.layout.ProjectCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;

public class ProjectsController implements Initializable {
    private Dialog<String> addDialog = new Dialog<String>();

    @FXML private Button addButton;
    @FXML private ListView<String> listView;

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		Projects projects = Projects.getInstance();
        listView.setItems(projects.get());
        listView.setCellFactory((list) -> new ProjectCell());
	};

    @FXML
    public void addProject() {
        try {
            AddProjectController addProjectController = new AddProjectController(addDialog);
            DialogPane pane = (DialogPane) App.load("createProjectDialog", addProjectController);
            App.applyCss(pane, "createProjectDialog");
            addDialog.setDialogPane(pane);
            Optional<String> response = addDialog.showAndWait();;
            if (response.isPresent() && !response.get().isEmpty()) {
                String projectName = response.get();
                new Project(projectName);
                Projects.getInstance().add(projectName);
            };
        } catch (IOException exception) {
            exception.printStackTrace();
        };
    };
};
