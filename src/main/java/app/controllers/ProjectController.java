package app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import app.App;
import app.core.Project;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;

public class ProjectController implements Initializable {
    private Project project;

    @FXML private TreeView<String> treeView;

    public ProjectController(String name) {
        this.project = new Project(name);
    };

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        treeView.setRoot(this.project.get());
    };

    @FXML 
    public void back() {
        try {
            App.setRoot("projects");
        } catch (Exception e) {
            e.printStackTrace();
        };
    };
};
