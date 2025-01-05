package app.controllers;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import app.App;
import app.core.Node;
import app.core.Project;
import app.core.Request;
import app.core.RequestType;
import app.core.Response;
import app.layout.HeaderHighlighter;
import app.layout.JsonHighlighter;
import app.layout.TableCellTextField;
//import app.layout.TableCellTextField;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

public class ProjectController implements Initializable {
    private Project project;
    private Request request;

    private JsonHighlighter bodyJsonHighlighter;
    private JsonHighlighter responseBodyJsonHighlighter;
    private HeaderHighlighter responseHeaderHighlighter;
    
    private ObservableList<Pair<String, String>> headersList;

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
        //#region Loading
        this.storingLabel.setText("Salvo!");
        this.project.setOnRequestStoring(() -> {
            this.backButton.setDisable(true);
            this.storingLabel.setText("Salvando...");
            this.storingProgressIndicator.setVisible(true);
            this.storingProgressIndicator.setPrefWidth(15);
            HBox.setMargin(
                this.storingProgressIndicator, 
                new Insets(0, 7, 0, 0)
            );
        });
        this.project.setOnStored(() -> {
            this.backButton.setDisable(false);
            this.storingLabel.setText("Salvo!");
            this.storingProgressIndicator.setVisible(false);
            this.storingProgressIndicator.setPrefWidth(0);
            HBox.setMargin(
                this.storingProgressIndicator, 
                new Insets(0, 0, 0, 0)
            );
        });
        //#endregion
        //#region Highlighters
        this.bodyJsonHighlighter = new JsonHighlighter(this.bodyTextFlow, this.bodyTextArea, this.bodyTextFlowScrollPane);
        // this.bodyJsonHighlighter.setText(
        //     "{\n" +
        //     "\t\"name\": \"Marcel\",\n" +
        //     "\t\"student\": true,\n" +
        //     "\t\"age\": 22\n" +
        //     "}"
        // );

        this.responseBodyJsonHighlighter = new JsonHighlighter(this.responseBodyTextFlow, this.responseBodyTextArea, this.responseBodyTextFlowScrollPane);
        // this.responseBodyJsonHighlighter.setText(
        //     "{\n" +
        //     "\t\"message\": \"success\",\n" +
        //     "\t\"code\": 200,\n" +
        //     "}"
        // );

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
        //#endregion
        //#region ChoiceBox
        this.requestTypeChoiceBox.getItems().addAll(RequestType.values());
        this.requestTypeChoiceBox.getSelectionModel().select(0);
        //#endregion
        //#region Tabs
        this.tabPane.heightProperty().addListener((event, old, current) -> {
            this.headerTableView.setPrefHeight(current.doubleValue());
            this.headerTableView.setMaxHeight(current.doubleValue());
        });

        this.tabPane.widthProperty().addListener((event, old, current) -> {
            this.headerTableView.setPrefWidth(current.doubleValue());
            this.headerTableView.setMaxWidth(current.doubleValue());
        });
        //#endregion
 
        this.configTreeView();

        //#region TitledPane
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
        //#endregion

        this.headersList = FXCollections.observableArrayList();
        this.headerTableView.setItems(headersList);

        this.keysTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKey()));
        this.keysTableColumn.setCellFactory(column -> new TableCellTextField((index, current) -> {
            if(index >= 0 && index < headersList.size()) {
                Pair<String, String> oldPair = headersList.get(index);
                Pair<String, String> newPair = new Pair<>(current, oldPair.getValue());
                headersList.set(index, newPair);
                addNewRow();
            };
        }));

        this.valuesTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue()));
        this.valuesTableColumn.setCellFactory(column -> new TableCellTextField((index, current) -> {
            if(index >= 0 && index < headersList.size()) {
                Pair<String, String> oldPair = headersList.get(index);
                Pair<String, String> newPair = new Pair<>(oldPair.getKey(), current);
                headersList.set(index, newPair);
                addNewRow();
            };
        }));

        // this.headerTableView.sortPolicyProperty().set(table -> {
        //     FXCollections.sort(headersList, (pair1, pair2) -> {
        //         boolean isPair1Empty = pair1.getKey().isEmpty() && pair1.getValue().isEmpty();
        //         boolean isPair2Empty = pair2.getKey().isEmpty() && pair2.getValue().isEmpty();

        //         if(isPair1Empty && !isPair2Empty) return 1;
        //         if(!isPair1Empty && isPair2Empty) return -1;

        //         Comparator<Pair<String, String>> comparator = table.getComparator();
        //         return comparator == null ? 0 : comparator.compare(pair1, pair2);
        //     });
        //     return true;
        // });
    };

    public void select(Request request) {
        this.blankVBox.setVisible(false);
        this.request = request;
        headersList.clear();
        this.request.getHeaders().forEach((key, value) -> 
            headersList.add(new Pair<>(key, value))
        );
        
        this.bodyJsonHighlighter.setText(this.request.bodyProperty());
        ObjectProperty<Response> lastResponse = this.request.lastResponseProperty();
        
        if(lastResponse.get() != null) {
            this.responseBodyJsonHighlighter.setText(
                new SimpleStringProperty(lastResponse.get().getMessage())
            );
    
            // TODO: Finish it
            // this.responseHeaderHighlighter.setText(
            //     new SimpleStringProperty(lastResponse.get())
            // );
        };
       
        addNewRow();
    };

    public void configTreeView() {
        this.treeView.getSelectionModel().selectedItemProperty().addListener((event, old, current) -> {
            if(current instanceof Request) {
                this.select((Request) current);
            };
        });

        this.treeView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            treeView.getSelectionModel().clearSelection();
        });

        ContextMenu contextMenu = new ContextMenu();

        MenuItem addRequest = new MenuItem("Adicionar requisição");
        MenuItem addFolder = new MenuItem("Adicionar pasta");
        MenuItem renameItem = new MenuItem("Renomear");
        MenuItem deleteItem = new MenuItem("Apagar");

        contextMenu.getItems().addAll(addRequest, addFolder, renameItem, deleteItem);

        treeView.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

                if(selectedItem == null) {
                    renameItem.setVisible(false);
                    deleteItem.setVisible(false);
                    addRequest.setVisible(true);
                    addFolder.setVisible(true);
                } else if(selectedItem instanceof Request) {
                    renameItem.setVisible(true);
                    deleteItem.setVisible(true);
                    addRequest.setVisible(false);
                    addFolder.setVisible(false);
                } else {
                    renameItem.setVisible(true);
                    deleteItem.setVisible(true);
                    addRequest.setVisible(true);
                    addFolder.setVisible(true);
                };

                contextMenu.show(treeView, event.getScreenX() + 30.0, event.getScreenY() + 10.0);
            } else if(event.getButton() == MouseButton.PRIMARY) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if(selectedItem instanceof Request) {
                    this.select((Request) selectedItem);
                };
            };
        });

        treeView.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if(event.getCode() == KeyCode.ENTER) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if(selectedItem instanceof Request){
                    this.select((Request) selectedItem);
                };
            };
        });
        
        addRequest.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if(selectedItem instanceof Node) addNewRequest(selectedItem);
            else addNewRequest(this.project.get());
        });

        addFolder.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if(selectedItem instanceof Node) addNewFolder(selectedItem);
            else addNewFolder(this.project.get());
        });

        renameItem.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if(selectedItem != null && selectedItem instanceof Node) {
                Node selectedNode = (Node) selectedItem;

                TextField textField = new TextField(selectedNode.getValue());
             
                textField.setStyle(
                    "-fx-border-radius: 0px;" +
                    "border-radius: 0px;" +
                    "-fx-background-radius: 0px;" +
                    "-fx-border-width: 0px;" +
                    "border-width: 0px;" + 
                    "background-color: #ce3802;" +
                    "-fx-background-color: #ce3802;"
                );

                textField.setMinWidth(treeView.getWidth());
                textField.setMinHeight(22);

                String oldValue = selectedNode.getValue();
                selectedNode.setValue("");

                textField.setOnAction(e -> {
                    Node updatedNode = selectedNode.rename(textField.getText());

                    this.treeView.getSelectionModel().select(updatedNode);
                    if(updatedNode == selectedNode) selectedNode.setValue(oldValue);

                    selectedNode.setGraphic(null);
                });

                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if(!isNowFocused) {
                        Node updatedNode = selectedNode.rename(textField.getText());
                        if(updatedNode == selectedNode) selectedNode.setValue(oldValue);
                        selectedNode.setGraphic(null);
                    };
                });
                
                selectedNode.setGraphic(textField);
                textField.requestFocus();
                textField.selectAll();
            };
        });

        deleteItem.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if(
                selectedItem != null && 
                selectedItem.getParent() != null && 
                selectedItem instanceof Node &&
                selectedItem instanceof Node
            ) {
                Node prent = (Node) selectedItem.getParent();
                Node selectedNode = (Node) selectedItem;
                prent.remove(selectedNode);
            };
        });

        this.treeView.setRoot(this.project.get());
    };

    private void addNewRequest(TreeItem<String> selectedItem) {
        if(selectedItem != null && selectedItem instanceof Node) {
            Node selectedNode = (Node) selectedItem;

            String name = "Nova requisição";
            int index = 1;

            if(selectedNode.childExists(name)) {
                while(selectedNode.childExists(name + " (" + index + ")")) {
                    index++;
                    if(index > 100000) return;
                };

                name = name + " (" + index + ")";
            };

            Request newRequest = new Request(name);
            selectedNode.add(newRequest);
            selectedNode.setExpanded(true);
        };
    };

    private void addNewFolder(TreeItem<String> selectedItem) {
        if(selectedItem != null && selectedItem instanceof Node) {
            Node selectedNode = (Node) selectedItem;

            String name = "Nova pasta";
            int index = 1;

            if(selectedNode.childExists(name)) {
                while(selectedNode.childExists(name + " (" + index + ")")) {
                    index++;
                    if(index > 100000) return;
                };

                name = name + " (" + index + ")";
            };

            Node newNode = new Node(name);
            selectedNode.add(newNode);
            selectedNode.setExpanded(true);
        };
    };

    private void addNewRow() {
        if(
            this.headersList.size() == 0 || 
            (
                !this.headersList.get(this.headersList.size() - 1).getKey().isBlank() && 
                !this.headersList.get(this.headersList.size() - 1).getValue().isBlank()
            )
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
