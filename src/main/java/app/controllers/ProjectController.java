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
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.TextField;
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

    private InvalidationListener requestTypeChoiceBoxListener;
    private ChangeListener<? super Response> lastResponseListener;

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

    @FXML private ChoiceBox<String> requestTypeChoiceBox;
    @FXML private TextField projectUrlInputBox;

    @FXML private TabPane tabPane;
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
        this.bodyJsonHighlighter = new JsonHighlighter(
            this.bodyTextFlow, 
            this.bodyTextArea, 
            this.bodyTextFlowScrollPane
        );

        this.responseBodyJsonHighlighter = new JsonHighlighter(
            this.responseBodyTextFlow, 
            this.responseBodyTextArea, 
            this.responseBodyTextFlowScrollPane
        );

        this.responseHeaderHighlighter = new HeaderHighlighter(
            this.responseHeaderTextFlow, 
            this.responseHeaderTextArea, 
            this.responseHeaderTextFlowScrollPane
        );

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
            (Request request) -> this.select(request),
            (Request request) -> {
                if(this.request == request) {
                    this.unselect();
                };
            }
        );
    };

    public void unselect() {
        if(this.request != null) {
            this.blankVBox.setVisible(true);
            this.bodyJsonHighlighter.unbindBidirectional(this.request.bodyProperty());
            ObjectProperty<Response> lastResponse = this.request.lastResponseProperty();
            
            if(lastResponseListener != null) {
                lastResponse.removeListener(lastResponseListener);
            };

            if(lastResponse.get() != null) {
                this.responseBodyJsonHighlighter.unbindBidirectional(
                    lastResponse.get().messageProperty()
                );
        
                this.responseHeaderHighlighter.unbindBidirectional(
                    lastResponse.get().headerProperty()
                );
            };

            this.projectUrlInputBox.textProperty().unbindBidirectional(this.request.urlProperty());;
            this.requestTypeChoiceBox.getItems().clear();

            if(this.requestTypeChoiceBoxListener != null) {
                this.requestTypeChoiceBox.valueProperty().removeListener(this.requestTypeChoiceBoxListener);
            };    
 
            this.request = null;
        };
    };

    public void select(Request request) {
        this.unselect();
        this.blankVBox.setVisible(false);
        this.request = request;
        this.bodyJsonHighlighter.bindBidirectional(this.request.bodyProperty());
        ObjectProperty<Response> lastResponse = this.request.lastResponseProperty();

        this.lastResponseListener = (event, old, current) -> {
            this.responseBodyJsonHighlighter.bindBidirectional(
                current.messageProperty()
            );
    
            this.responseHeaderHighlighter.bindBidirectional(
                current.headerProperty()
            );
        };

        lastResponse.addListener(this.lastResponseListener);

        if(lastResponse.get() != null) {
            this.responseBodyJsonHighlighter.bindBidirectional(
                lastResponse.get().messageProperty()
            );
    
            this.responseHeaderHighlighter.bindBidirectional(
                lastResponse.get().headerProperty()
            );
        } else {
            this.responseBodyJsonHighlighter.clear();
            this.responseHeaderHighlighter.clear();
        };

        this.projectUrlInputBox.textProperty().bindBidirectional(this.request.urlProperty());

        this.requestTypeChoiceBox.getItems().clear();
        for(RequestType type : RequestType.values()) {
            this.requestTypeChoiceBox.getItems().add(type.toString());
        };
        this.requestTypeChoiceBox.getSelectionModel().select(this.request.typeProperty().get().name());

        this.requestTypeChoiceBoxListener = (event) -> {
            if(this.requestTypeChoiceBox.getValue() == null) return;
            this.request.typeProperty().set(RequestType.valueOf(
                this.requestTypeChoiceBox.getValue()
            ));
        }; 
        
        this.requestTypeChoiceBox.valueProperty().addListener(this.requestTypeChoiceBoxListener);

        this.headerTableView.setItems(this.request.headerProperty());
        ProjectControllerHeaderTableUtils.cleanupAndAddEmptyRowIfNeeded(this.headerTableView.getItems());
    };

    @FXML
    public void submit() {
        try {
            if(this.request != null) {
                this.request.submit();
            };
        } catch (Exception e) {
            e.printStackTrace();
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
