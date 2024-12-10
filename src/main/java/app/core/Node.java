package app.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javafx.scene.control.TreeItem;

public class Node extends TreeItem<String> implements Externalizable {
    public Node(String name) {
        super(name);
    };

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // [TODO] Serialize nodes. 
        // [TIP] Need to store others node!
        // [CAUTION] This funcion is already called recursively!
    };

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // [TODO] Deserialize nodes. 
        // [TIP] Need to store others node!
        // [CAUTION] This funcion is already called recursively!
    };

    public boolean childExists(String name) {
        // [TODO] Check if any children of this node have this name.
        return false;
    };
};
