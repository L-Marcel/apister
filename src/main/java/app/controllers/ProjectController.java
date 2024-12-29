package app.controllers;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import app.App;
import app.core.Node;
import app.core.Project;
import app.core.Request;
import app.layout.TableCellTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Pair;

public class ProjectController implements Initializable {
    private Project project;
    private Request request;
    private ObservableList<Pair<String, String>> headersList;

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
        configContextMenu();

        this.responseTitledPane.expandedProperty().addListener((event, old, current) -> {
            if (current) responseAnchorPane.setMaxHeight(AnchorPane.USE_COMPUTED_SIZE);
            else responseAnchorPane.setMaxHeight(0);
            headerTableView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        });

        this.headerTableView.setEditable(true);
        this.headersList = FXCollections.observableArrayList();
        this.headerTableView.setItems(headersList);

        this.keysTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKey()));
        this.keysTableColumn.setCellFactory(column -> new TableCellTextField());
        this.keysTableColumn.setOnEditCommit(event -> {
            System.out.println("Key");
            Pair<String, String> oldPair = event.getRowValue();
            Pair<String, String> newPair = new Pair<>(event.getNewValue(), oldPair.getValue());
            headersList.set(event.getTablePosition().getRow(), newPair);
            addNewRow();
        });

        this.valuesTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue()));
        this.valuesTableColumn.setCellFactory(column -> new TableCellTextField());
        this.valuesTableColumn.setOnEditCommit(event -> {
            System.out.println("Value");
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
        Request req = new Request("Requisição");
        Node node = new Node("Pasta");
        this.project.get().getChildren().add(req);
        this.project.get().getChildren().add(node);

        req.getHeaders().put("Testing", "The Headers");
        req.getHeaders().put("Another", "Test");
        this.select(req);
    };

    public void select(Node node) {
        if(node instanceof Request) {
            this.request = (Request) node;
            headersList.clear();
            this.request.getHeaders().forEach((key, value) -> 
                headersList.add(new Pair<>(key, value))
            );
            addNewRow();
        };
    };

    public void configContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addRequest = new MenuItem("Add Request");
        MenuItem addFolder = new MenuItem("Add Folder");
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem deleteItem = new MenuItem("Delete");

        contextMenu.getItems().addAll(addRequest, addFolder, renameItem, deleteItem);

        treeView.setOnMouseClicked(event -> {
            if (event.getButton() ==  MouseButton.SECONDARY) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

                if (selectedItem == null) {
                    contextMenu.hide();
                    return;
                }
                if (selectedItem instanceof Request) {
                    addRequest.setVisible(false);
                    addFolder.setVisible(false);
                }
                else {
                    addRequest.setVisible(true);
                    addFolder.setVisible(true);
                }
                contextMenu.show(treeView, event.getScreenX() + 30.0, event.getScreenY() + 10.0);
            }
        });
        
        addRequest.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem instanceof Node) addNewRequest(selectedItem);
        });

        addFolder.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem instanceof Node) addNewFolder(selectedItem);
        });

        renameItem.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                TextField textField = new TextField(selectedItem.getValue());
                textField.setOnAction(e -> {
                    selectedItem.setValue(textField.getText());
                    selectedItem.setGraphic(null);// Remove o campo de texto após renomear
                });
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        selectedItem.setValue(textField.getText());
                        selectedItem.setGraphic(null);
                    }
                });
                selectedItem.setGraphic(textField); // Mostra o campo de texto na célula
                textField.requestFocus(); // Coloca o foco no campo de texto
                textField.selectAll();
            }
        });

        deleteItem.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getParent() != null) {
                selectedItem.getParent().getChildren().remove(selectedItem);
            }
        });

        treeView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (!treeView.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
                        treeView.getSelectionModel().clearSelection();
                    }
                });
            }
        });
    }

    private void addNewRequest(TreeItem<String> selectedItem) {
        if (selectedItem != null) {
            Request newRequest = new Request("New Request");
            selectedItem.getChildren().add(newRequest);
            selectedItem.setExpanded(true);
        }
    }

    private void addNewFolder(TreeItem<String> selectedItem) {
        if (selectedItem != null) {
            Node newNode = new Node("New Folder");
            selectedItem.getChildren().add(newNode);
            selectedItem.setExpanded(true);
        }
    }

    private void addNewRow() {
        if (!this.headersList.isEmpty()) {
            Pair<String, String> lastPair = this.headersList.get(this.headersList.size() - 1);
            if (lastPair.getKey().isEmpty() && lastPair.getValue().isEmpty()) {
                return;
            }
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
