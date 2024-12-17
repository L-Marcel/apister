package app.controllers;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import app.App;
import app.core.Node;
import app.core.Project;
import app.core.Request;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class ProjectController implements Initializable {
    private Project project;
    private Request request;

    @FXML private TableView<Map.Entry<String, String>> headerTableView;
    @FXML private TableColumn<Map.Entry<String, String>, String> keysTableColumn;
    @FXML private TableColumn<Map.Entry<String, String>, String> valuesTableColumn;
    @FXML private AnchorPane responseAnchorPane;
    @FXML private TitledPane responseTitledPane;
    @FXML private TreeView<String> treeView;

    public ProjectController(String name) {
        this.project = new Project(name);
    };

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        treeView.setRoot(this.project.get());
        responseTitledPane.expandedProperty().addListener((event, old, current) -> {
            if (current) responseAnchorPane.setMaxHeight(AnchorPane.USE_COMPUTED_SIZE);
            else responseAnchorPane.setMaxHeight(0);
        });

        this.keysTableColumn.setCellFactory(cell -> {
            return new TableCell<Map.Entry<String, String>, String>();
        });

        this.valuesTableColumn.setCellFactory(cell -> {
            return new TableCell<Map.Entry<String, String>, String>();
        });

        Request req = new Request("abc");
        req.getHeaders().put("Authorization", "Bearer");
        this.select(req);
    };

    public void select(Node node) {
        if(node instanceof Request) {
            this.request = (Request) node;
            
            this.headerTableView.setItems(
                FXCollections.observableArrayList(
                    request.getHeaders().entrySet()
                )
            );
        };
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
