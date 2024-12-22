package app.controllers.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import app.App;
import app.controllers.components.RemoveDialogProjectController;
import app.core.Project;
import app.controllers.ProjectController;
import app.core.Projects;
import app.layout.Dialog;
import app.layout.ProjectCell;
import app.storage.Storable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

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
            Dialog<String> dialog = new Dialog<String>(
                    null,
                    "components/removeProjectDialog",
                    new RemoveDialogProjectController()
            );
            String confirm = dialog.showAndGet();
            if (confirm == null) {
                return;
            }
            if (confirm.equals("confirm")) {
                Projects projects = Projects.getInstance();
                String name = this.cell.getItem();
                File file = new File("data/project_" + name + ".dat");
                if (file.delete()) {
                    projects.remove(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        };
    };

    @FXML
    public void export() {
        try {
            Projects projects = Projects.getInstance();
            String name = this.cell.getItem();
            File file;
            FileOutputStream fileOut;
            ObjectOutputStream objectOutputStream;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exportar projeto");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Dados", "*.dat"));
            fileChooser.setInitialFileName("project_" + name + ".dat");
            file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }
            fileOut = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOut);
            Project project = new Project(name);
            objectOutputStream.writeUTF("project_" + name + ".dat");
            objectOutputStream.writeObject(project.get());
            objectOutputStream.close();
            fileOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
};
