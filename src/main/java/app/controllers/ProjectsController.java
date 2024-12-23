package app.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import app.controllers.components.AddDialogProjectController;
import app.core.Node;
import app.core.Project;
import app.core.Projects;
import app.errors.InvalidInput;
import app.layout.Dialog;
import app.layout.ProjectCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

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
        } catch (IOException e) {
            e.printStackTrace();
        };
    };

    @FXML
    public void importProject() {
        try {
            Projects projects = Projects.getInstance();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Importe um projeto");
            File file = fileChooser.showOpenDialog(null);
            if (file == null) {
                return;
            }
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            String name = objectIn.readUTF();
            Node node = (Node) objectIn.readObject();
            if (node == null) { return;}
            try {
                Projects.validate(name);
            } catch (InvalidInput e) {
                System.err.println(e.getMessage());
                try {
                    Dialog<String> dialog = new Dialog<String>(
                            "Defina um novo nome para o projeto importado",
                            "components/addProjectDialog",
                            new AddDialogProjectController()
                    );
                    name = dialog.showAndGet();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return;
                }
            }
            Project project = new Project(name,node);
            projects.add(name);
            project = null; //solução gambiarra porque eu não tô conseguindo pensar numa solução pra isso agora
            objectIn.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
};
