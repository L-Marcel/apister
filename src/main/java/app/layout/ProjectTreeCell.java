package app.layout;

import app.core.Node;
import app.core.Request;
import app.core.RequestType;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;

public class ProjectTreeCell extends TreeCell<String> {
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(!empty) {
            this.setText(item);

            if(this.getTreeItem() instanceof Node) {
                Node node = (Node) this.getTreeItem();

                if(node.isEditing()) {
                    //#region Editing
                    TextField textField = new TextField(node.getOldValue());

                    textField.getStyleClass().add("rename-request-text-field");
                    textField.setMinWidth(this.getTreeView().getWidth());
                    textField.setMinHeight(22);
    
                    textField.setOnAction(e -> {
                        Node updatedNode = node.rename(textField.getText());
                        this.getTreeView().getSelectionModel().select(updatedNode);
                        if(updatedNode == node) node.stopEdit();
                    });
    
                    textField.focusedProperty().addListener(
                        (obs, wasFocused, isNowFocused) -> {
                            if(!isNowFocused) {
                                Node updatedNode = node.rename(textField.getText());
                                if(updatedNode == node) node.stopEdit();
                            };
                        }
                    );

                    this.setText("");
                    this.setGraphic(textField);
                    Platform.runLater(() -> {
                        textField.requestFocus();
                        textField.selectAll();
                    });
                    //#endregion
                } else if(node instanceof Request) {
                    //#region Request
                    Request request = (Request) this.getTreeItem();
                    Label label = new Label();
    
                    label.getStyleClass().add("request-tag");
                    label.getStyleClass().add(getTypeClassName(request.getType()));
                    label.textProperty().bind(request.typeProperty().asString());
                    label.textProperty().addListener((event, old, current) -> {
                        label.getStyleClass().clear();
                        label.getStyleClass().add("request-tag");
                        label.getStyleClass().add(
                            getTypeClassName(RequestType.valueOf(current))
                        );
                    });
                    
                    this.setGraphic(label);
                    //#endregion
                };
            };
        } else {
            this.setText(null);
            this.setGraphic(null);
        };
    };

    private String getTypeClassName(RequestType type) {
        switch(type) {
            case GET:
                return "get-tag";
            case POST:
                return "post-tag";
            case PUT:
                return "put-tag";
            case DELETE:
                return "delete-tag";
            default:
                return "get-tag";
        }
    };
};
