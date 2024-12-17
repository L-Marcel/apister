package app.controllers.components;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import app.App;
import app.controllers.ProjectController;
import app.core.Projects;
import app.layout.ProjectCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ProjectCellController implements Initializable {
    private ProjectCell cell;

    @FXML private Label nameLabel;
    @FXML private Label pathLabel;

    public ProjectCellController(ProjectCell cell) {
        this.cell = cell;
    };

    @Override
	public void initialize(URL url, ResourceBundle resource) {
        String name = this.cell.getItem();
		this.nameLabel.setText(name);
        Path path = Path.of("data/projects_" + name + ".dat");
        this.pathLabel.setText(path.toAbsolutePath().toString());
	};

    @FXML
    public void open() {
        try {
            String name = this.cell.getItem();
            App.setRoot("project", new ProjectController(name));
        } catch (Exception e) {
            e.printStackTrace();
        };
    };

    @FXML
    public void remove() {
        try {
            Projects projects = Projects.getInstance();
            String name = this.cell.getItem();
            File file = new File("data/project_" + name + ".dat");
            if(file.delete()) projects.remove(name);
        } catch (Exception e) {
            e.printStackTrace();
        };
    };
};
