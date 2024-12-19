package app.controllers;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import app.App;
import app.core.Node;
import app.core.Project;
import app.core.Request;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Pair;

public class ProjectController implements Initializable {
    private Project project;
    private Request request;
    private ObservableList<Pair<String, String>> headersList;

    @FXML private TextArea responseHeaderTextArea;
    @FXML private TextArea responseBodyTextArea;
    @FXML private AnchorPane rightAnchorPane;
    @FXML private TableView<Pair<String, String>> headerTableView;
    @FXML private TableColumn<Pair<String, String>, String> keysTableColumn;
    @FXML private TableColumn<Pair<String, String>, String> valuesTableColumn;
    @FXML private AnchorPane responseAnchorPane;
    @FXML private TitledPane responseTitledPane;
    @FXML private TreeView<String> treeView;

    public ProjectController(String name) {
        this.project = new Project(name);
    };

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        this.treeView.setRoot(this.project.get());
        this.treeView.setShowRoot(false);

        this.rightAnchorPane.widthProperty().addListener((observable, old, current) -> {
            double half = current.doubleValue() / 2.0d;
            this.keysTableColumn.setPrefWidth(half + 0.5);
            this.valuesTableColumn.setPrefWidth(half + 0.5);
        });

        this.responseTitledPane.expandedProperty().addListener((event, old, current) -> {
            if (current) responseAnchorPane.setMaxHeight(AnchorPane.USE_COMPUTED_SIZE);
            else responseAnchorPane.setMaxHeight(0);
            headerTableView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        });

        this.headersList = FXCollections.observableArrayList();
        this.headerTableView.setItems(headersList);

        this.keysTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKey()));
        this.keysTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.keysTableColumn.setOnEditCommit(event -> {
            Pair<String, String> oldPair = event.getRowValue();
            Pair<String, String> newPair = new Pair<>(event.getNewValue(), oldPair.getValue());
            headersList.set(event.getTablePosition().getRow(), newPair);
            addNewRow();
        });

        this.valuesTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue()));
        this.valuesTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.valuesTableColumn.setOnEditCommit(event -> {
            Pair<String, String> oldPair = event.getRowValue();
            Pair<String, String> newPair = new Pair<>(oldPair.getKey(), event.getNewValue());
            headersList.set(event.getTablePosition().getRow(), newPair);
            addNewRow();
        });

        this.headerTableView.sortPolicyProperty().set(table -> {
            FXCollections.sort(headersList, (pair1, pair2) -> {
                boolean isPair1Empty = pair1.getKey().isEmpty() && pair1.getValue().isEmpty();
                boolean isPair2Empty = pair2.getKey().isEmpty() && pair2.getValue().isEmpty();

                if (isPair1Empty && !isPair2Empty) return 1;
                if (!isPair1Empty && isPair2Empty) return -1;

                Comparator<Pair<String, String>> comparator = table.getComparator();
                return comparator == null ? 0 : comparator.compare(pair1, pair2);
            });
            return true;
        });


        // [TIP] Parte estática para testes
        Request req = new Request("abc");
        this.select(req);
    };

    public void select(Node node) {
        if(node instanceof Request) {
            this.request = (Request) node;
            headersList.clear();
            this.request.getHeaders().forEach((key, value) -> 
                headersList.add(new Pair<>(key, value))
            );
            this.headersList.add(new Pair<>("",""));
        };
    };

    private void addNewRow() {
        if (this.headersList.size() > 0 && 
            !this.headersList.get(this.headersList.size() - 1).getKey().isEmpty() && 
            !this.headersList.get(this.headersList.size() - 1).getValue().isEmpty()
            ) {
            this.headersList.add(new Pair<>("", ""));
        }
    }

    @FXML 
    public void back() {
        try {
            App.setRoot("projects");
        } catch (Exception e) {
            e.printStackTrace();
        };
    };
};
