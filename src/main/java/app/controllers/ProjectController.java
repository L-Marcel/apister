package app.controllers;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import app.App;
import app.core.Node;
import app.core.Project;
import app.core.Request;
import app.core.RequestType;
import app.layout.HeaderHighlighter;
import app.layout.JsonHighlighter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

public class ProjectController implements Initializable {
    private Project project;
    private Request request;

    private JsonHighlighter bodyJsonHighlighter;
    private JsonHighlighter responseBodyJsonHighlighter;
    private HeaderHighlighter responseHeaderHighlighter;
    
    private ObservableList<Pair<String, String>> headersList;

    @FXML private ScrollPane bodyTextFlowScrollPane;
    @FXML private TextArea bodyTextArea;
    @FXML private TextFlow bodyTextFlow;

    @FXML private ScrollPane responseBodyTextFlowScrollPane;
    @FXML private TextArea responseBodyTextArea;
    @FXML private TextFlow responseBodyTextFlow;

    @FXML private ScrollPane responseHeaderTextFlowScrollPane;
    @FXML private TextArea responseHeaderTextArea;
    @FXML private TextFlow responseHeaderTextFlow;

    @FXML private TabPane tabPane;
    @FXML private ChoiceBox<RequestType> requestTypeChoiceBox;
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
        this.bodyJsonHighlighter = new JsonHighlighter(this.bodyTextFlow, this.bodyTextArea, this.bodyTextFlowScrollPane);
        this.bodyJsonHighlighter.setText(
            "{\n" +
            "\t\"name\": \"Marcel\",\n" +
            "\t\"student\": true,\n" +
            "\t\"age\": 22\n" +
            "}"
        );

        this.responseBodyJsonHighlighter = new JsonHighlighter(this.responseBodyTextFlow, this.responseBodyTextArea, this.responseBodyTextFlowScrollPane);
        this.responseBodyJsonHighlighter.setText(
            "{\n" +
            "\t\"message\": \"success\",\n" +
            "\t\"code\": 200,\n" +
            "}"
        );

        this.responseHeaderHighlighter = new HeaderHighlighter(this.responseHeaderTextFlow, this.responseHeaderTextArea, this.responseHeaderTextFlowScrollPane);
        this.responseHeaderHighlighter.setText(   
            "HTTP/1.1 200 OK\n" +
            "Date: Thu, 19 Dec 2024 12:00:00 GMT\n" +
            "Server: Apache/2.4.41 (Ubuntu)\n" +
            "Content-Type: application/json\n" +
            "Content-Length: 348\n" +
            "Connection: keep-alive\n" +
            "Cache-Control: max-age=3600\n" +
            "ETag: \"123456789abcdef\"\n" +
            "Last-Modified: Wed, 18 Dec 2024 10:30:00 GMT"
        );

        this.requestTypeChoiceBox.getItems().addAll(RequestType.values());
        this.requestTypeChoiceBox.getSelectionModel().select(0);

        this.tabPane.heightProperty().addListener((event, old, current) -> {
            this.headerTableView.setPrefHeight(current.doubleValue());
            this.headerTableView.setMaxHeight(current.doubleValue());
        });

        this.tabPane.widthProperty().addListener((event, old, current) -> {
            this.headerTableView.setPrefWidth(current.doubleValue());
            this.headerTableView.setMaxWidth(current.doubleValue());
        });

        Node folder = new Node("tests");
        Node another = new Node("another");
        Node anotherOne = new Node("one");
        another.getChildren().add(anotherOne);
        Request req = new Request("abc");
        folder.getChildren().add(req);
        folder.getChildren().add(new Request("now"));
        folder.getChildren().add(another);
        Request p = new Request("final");
        Node fakeRoot = new Node("root");
        fakeRoot.getChildren().add(folder);
        fakeRoot.getChildren().add(p);
        this.treeView.setRoot(fakeRoot);
        this.treeView.setShowRoot(false);

        this.rightAnchorPane.widthProperty().addListener((observable, old, current) -> {
            double half = current.doubleValue() / 2.0d;
            this.keysTableColumn.setPrefWidth(half + 0.5);
            this.valuesTableColumn.setPrefWidth(half + 0.5);
        });

        this.responseTitledPane.expandedProperty().addListener((event, old, current) -> {
            if(current) responseAnchorPane.setMaxHeight(AnchorPane.USE_COMPUTED_SIZE);
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

                if(isPair1Empty && !isPair2Empty) return 1;
                if(!isPair1Empty && isPair2Empty) return -1;

                Comparator<Pair<String, String>> comparator = table.getComparator();
                return comparator == null ? 0 : comparator.compare(pair1, pair2);
            });
            return true;
        });

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
        if(
            this.headersList.size() > 0 && 
            !this.headersList.get(this.headersList.size() - 1).getKey().isEmpty() && 
            !this.headersList.get(this.headersList.size() - 1).getValue().isEmpty()
        ) {
            this.headersList.add(new Pair<>("", ""));
        };
    };

    @FXML 
    public void back() {
        try {
            App.setRoot("projects");
        } catch(Exception e) {
            e.printStackTrace();
        };
    };
};
