package app.utils;

import app.core.Node;
import app.core.Project;
import app.core.Request;
import app.interfaces.TreeSelectCallback;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ProjectControllerTreeUtils {
    private static void addRequest(TreeItem<String> selectedItem) {
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

    private static void addFolder(TreeItem<String> selectedItem) {
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

    public static void configTree(
        Project project,
        TreeView<String> treeView,
        TreeSelectCallback select
    ) {
        treeView.getSelectionModel().selectedItemProperty().addListener((event, old, current) -> {
            if(current instanceof Request && select != null) {
                select.call((Request) current);
            };
        });

        treeView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
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
                if(selectedItem instanceof Request && select != null) {
                    select.call((Request) selectedItem);
                };
            };
        });

        treeView.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
            if(event.getCode() == KeyCode.ENTER) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if(selectedItem instanceof Request && select != null){
                    select.call((Request) selectedItem);
                };
            };
        });
        
        addRequest.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if(selectedItem instanceof Node) addRequest(selectedItem);
            else addRequest(project.get());
        });

        addFolder.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if(selectedItem instanceof Node) addFolder(selectedItem);
            else addFolder(project.get());
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

                    treeView.getSelectionModel().select(updatedNode);
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

        treeView.setRoot(project.get());
    };
};
