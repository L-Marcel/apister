package app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import app.App;
import app.core.HeaderEntry;
import app.core.Project;
import app.core.Request;
import app.core.RequestType;
import app.core.Response;
import app.layout.HeaderHighlighter;
import app.layout.JsonHighlighter;
import app.utils.ProjectControllerHeaderTableUtils;
import app.utils.ProjectControllerTreeUtils;
import app.utils.ProjectControllerUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class ProjectController implements Initializable {
    private Project project;
    private Request request;

    private JsonHighlighter bodyJsonHighlighter;
    private JsonHighlighter responseBodyJsonHighlighter;
    private HeaderHighlighter responseHeaderHighlighter;

    @FXML private Button backButton;
    @FXML private Label storingLabel;
    @FXML private ProgressIndicator storingProgressIndicator;
    
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
    @FXML private VBox blankVBox;
    @FXML private TableView<HeaderEntry> headerTableView;
    @FXML private TableColumn<HeaderEntry, String> keysTableColumn;
    @FXML private TableColumn<HeaderEntry, String> valuesTableColumn;
    @FXML private AnchorPane responseAnchorPane;
    @FXML private TitledPane responseTitledPane;
    @FXML private TreeView<String> treeView;

    public ProjectController(String name) {
        this.project = new Project(name);
    };

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        ProjectControllerUtils.configPanes(
            this.tabPane,
            this.rightAnchorPane,
            this.responseAnchorPane,
            this.responseTitledPane,
            this.headerTableView,
            this.keysTableColumn,
            this.valuesTableColumn
        );

        ProjectControllerUtils.configLoading(
            this.project,
            this.backButton,
            this.storingLabel,
            this.storingProgressIndicator
        );

        ProjectControllerHeaderTableUtils.configHeaderTable(
            this.headerTableView,
            this.keysTableColumn, 
            this.valuesTableColumn
        );

        ProjectControllerTreeUtils.configTree(
            this.project,
            this.treeView,
            (Request request) -> this.select(request)
        );
        
        this.bodyJsonHighlighter = new JsonHighlighter(this.bodyTextFlow, this.bodyTextArea, this.bodyTextFlowScrollPane);
        this.responseBodyJsonHighlighter = new JsonHighlighter(this.responseBodyTextFlow, this.responseBodyTextArea, this.responseBodyTextFlowScrollPane);
        this.responseHeaderHighlighter = new HeaderHighlighter(this.responseHeaderTextFlow, this.responseHeaderTextArea, this.responseHeaderTextFlowScrollPane);
        // this.responseHeaderHighlighter.setText(   
        //     "HTTP/1.1 200 OK\n" +
        //     "Date: Thu, 19 Dec 2024 12:00:00 GMT\n" +
        //     "Server: Apache/2.4.41 (Ubuntu)\n" +
        //     "Content-Type: application/json\n" +
        //     "Content-Length: 348\n" +
        //     "Connection: keep-alive\n" +
        //     "Cache-Control: max-age=3600\n" +
        //     "ETag: \"123456789abcdef\"\n" +
        //     "Last-Modified: Wed, 18 Dec 2024 10:30:00 GMT"
        // );

        this.requestTypeChoiceBox.getItems().addAll(RequestType.values());
        this.requestTypeChoiceBox.getSelectionModel().select(0);
    };

    public void select(Request request) {
        this.blankVBox.setVisible(false);
        this.request = request;
        this.bodyJsonHighlighter.setText(this.request.bodyProperty());
        ObjectProperty<Response> lastResponse = this.request.lastResponseProperty();
        
        if(lastResponse.get() != null) {
            this.responseBodyJsonHighlighter.setText(
                new SimpleStringProperty(lastResponse.get().getMessage())
            );
    
            this.responseHeaderHighlighter.setText(
                new SimpleStringProperty(lastResponse.get().getMessage())
            );
        };

        this.headerTableView.setItems(this.request.headerProperty());
        ProjectControllerHeaderTableUtils.addEmptyRowIfNeeded(this.headerTableView.getItems());
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
