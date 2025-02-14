package app.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import javafx.scene.control.TreeItem;

public class Node extends TreeItem<String> implements Externalizable {
    private String oldValue = "";
    private boolean editing = false;

    public Node() {};
    public Node(String name) {
        super(name);
    };

    //#region Externalizable
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.getValue());
        List<TreeItem<String>> children = this.getChildren();
        out.writeInt(children.size());

        for(TreeItem<String> child : children) {
           out.writeObject(child);
        };
    };

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setValue(in.readUTF());
        int size = in.readInt();

        for(int i = 0; i < size; i++) {
            TreeItem<String> child = (TreeItem<String>) in.readObject();
            this.getChildren().add(child);
        };
    };
    //#endregion
    //#region Edit
    public String getOldValue() {
        return this.oldValue;
    };

    public boolean isEditing() {
        return this.editing;
    };

    public void stopEdit() {
        this.editing = false;
        this.setValue(oldValue);
    };

    public void startEdit() {
        this.editing = true;
        this.oldValue = this.getValue();
        this.setValue("");
    };

    public Node rename(String name) {
        if(!(this.getParent() instanceof Node)) return this;

        Node parent = (Node) this.getParent();
        if(parent != null && !parent.exists(name) && !name.isBlank()) {
            Node node = new Node(name);
            node.getChildren().setAll(this.getChildren());
            parent.replace(this, node);
            return node;
        };

        return this;
    };
    //#endregion
    //#region Nodes
    private void sortNodes() {
        this.getChildren().sort((a, b) -> {
            return a.getValue().compareTo(b.getValue());
        });
    };

    public void add(Node node) {
        this.getChildren().add(node);
        this.sortNodes();
    };

    public void remove(Node node) {
        this.getChildren().remove(node);
        this.sortNodes();
    };

    public void replace(Node from, Node to) {
        this.getChildren().remove(from);
        this.getChildren().add(to);
        this.sortNodes();
    };

    public boolean exists(String name) {
        for(TreeItem<String> child : this.getChildren()) {
            if(child.getValue().equals(name)) return true;
        };
        
        return false;
    };
    //#endregion
};
