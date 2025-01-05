package app.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import app.core.Node;
import app.core.Project;
import app.core.Projects;
import app.layout.ProjectCell;
import app.layout.TextFieldDialog;
import app.log.Log;
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
            TextFieldDialog dialog = new TextFieldDialog(
                "Adicionando novo projeto",
                "Nome do projeto",
                "Nome disponível!",
                (String candidate) -> Projects.validate(candidate)
            );

            String name = dialog.showAndGet();
            if(name != null) {
                Log.print("Adding project \"" + name + "\"...");
                new Project(name);
                Projects.getInstance().add(name);
            };
        } catch(IOException e) {
            e.printStackTrace();
        };
    };

    @FXML
    public void importProject() {
        try {
            Projects projects = Projects.getInstance();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Importe um projeto");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Apister Project", "*.apis"));
            File file = fileChooser.showOpenDialog(null);
            if(file == null) return;

            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            String name = objectIn.readUTF();
            Node node = (Node) objectIn.readObject();
            objectIn.close();
            fileIn.close();

            TextFieldDialog dialog = new TextFieldDialog(
                "Importando projeto",
                "Nome do projeto",
                "Nome disponível!",
                name,
                (String candidate) -> Projects.validate(candidate)
            );

            name = dialog.showAndGet();
            if(name != null) {
                Log.print("Importing project \"" + name + "\" from \"" + file.getAbsolutePath() + "\"...");
                new Project(name ,node);
                projects.add(name);
            };
        } catch(Exception e) {
            e.printStackTrace();
        };
    };
};
