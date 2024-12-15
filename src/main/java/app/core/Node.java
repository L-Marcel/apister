package app.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import javafx.scene.control.TreeItem;

public class Node extends TreeItem<String> implements Externalizable {
    public Node(String name) {
        super(name);
    };

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(getValue());
        List<TreeItem<String>> children = getChildren();
        out.writeInt(children.size());

        for (TreeItem<String> child : children) {
           out.writeObject(child);
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setValue(in.readUTF());
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            TreeItem<String> child = (TreeItem<String>) in.readObject();
            getChildren().add(child);
        }
    };

    public boolean childExists(String name) {
        for (TreeItem<String> child : getChildren()) {
            if(child.getValue().equals(name)) return true;
        }
        return false;
    };
};
