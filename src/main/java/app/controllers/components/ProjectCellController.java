package app.controllers.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import app.App;
import app.core.Project;
import app.controllers.ProjectController;
import app.core.Projects;
import app.layout.ConfirmDialog;
import app.layout.ProjectCell;
import app.layout.TextFieldDialog;
import app.log.Log;
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
            Log.print("Openning project \"" + name + "\"...");
            App.setRoot("project", new ProjectController(name));
        } catch(Exception e) {
            e.printStackTrace();
        };
    };

    @FXML
    public void rename() {
        try {
            String name = this.cell.getItem();
        
            TextFieldDialog dialog = new TextFieldDialog(
                "Renomeando projeto",
                "Nome do projeto",
                "Nome disponível!",
                name,
                (String candidate) -> {
                    if(!candidate.equals(name)) Projects.validate(candidate);
                }
            );

            String newName = dialog.showAndGet();
            if(newName != null) {
                File old = new File("data/project_" + name + ".dat");
                File current = new File("data/project_" + newName + ".dat");
                Log.print("Renaming project from \"" + name + "\" to \"" + newName + "\"...");
                if(old.renameTo(current)) {
                    Projects.getInstance().replace(name, newName);
                };
            };
        } catch (Exception e) {
            e.printStackTrace();
        };
    };

    @FXML
    public void remove() {
        try {
            String name = this.cell.getItem();
            ConfirmDialog dialog = new ConfirmDialog(
                "Tem certeza que quer remover este projeto?",
                "Saiba que \"" + name + "\" pode se perder para sempre!"
            );
            
            if(dialog.showAndGet().booleanValue()) {
                Projects projects = Projects.getInstance();
                File file = new File("data/project_" + name + ".dat");
                Log.print("Removing project \"" + name + "\"...");
                if(file.delete()) projects.remove(name);
            };
        } catch(Exception e) {
            e.printStackTrace();
        };
    };

    @FXML
    public void export() {
        try {
            String name = this.cell.getItem();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exportar projeto");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Apister Project", "*.apis"));
            fileChooser.setInitialFileName(name + ".apis");
            File file = fileChooser.showSaveDialog(null);
            if(file == null) return;
            
            Log.print("Exporting project \"" + name + "\" to \"" + file.getAbsolutePath() + "\"...");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOut);
            Project project = new Project(name);
            objectOutputStream.writeUTF(name);
            objectOutputStream.writeObject(project.get());
            objectOutputStream.close();
            fileOut.close();
        } catch(Exception e) {
            e.printStackTrace();
        };
    };
};
