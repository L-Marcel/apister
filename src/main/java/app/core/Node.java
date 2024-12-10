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
        // [TODO] Serialização de um nó.

        // [TIP] Você precisará armazenar outros nós, talvez, se preferir
        // usando writeObject. Acontece que a lista de filhos não 
        // implementa Serializable, mas talvez seja convertível para
        // uma LinkedList. Ainda assim esse método aqui é necessário.
    };

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // [TODO] Deserialização de um nó.
        
        // [TIP] Você precisará obter outros nós, talvez, se preferir
        // usando readObject.
    };

    public boolean childExists(String name) {
        // [TODO] Verifica se tem algum filho com o nome buscado.
        return false;
    };
};
