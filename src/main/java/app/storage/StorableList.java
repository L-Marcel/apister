package app.storage;

import java.io.Serializable;
import java.util.LinkedList;

import app.log.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

public abstract class StorableList<T extends Serializable> implements Serializable {
    private Storage storage = Storage.getInstance();
    private String name;
    protected ObservableList<T> instances;

    @SuppressWarnings("unchecked")
    public StorableList(String name) {
        this.name = name;
        try {
            FileInputStream input = new FileInputStream("data/" + this.name + ".dat");  
            ObjectInputStream object = new ObjectInputStream(input);
            LinkedList<T> list = (LinkedList<T>) object.readObject();
            this.instances = FXCollections.observableList(list);
            Log.print("Storable", "Loadded " + this.instances.size() + " instances from \"" + this.name + "\".");
            object.close();         
        } catch(FileNotFoundException e) {
            Log.print("Storable", "Creating \"" + this.name + "\" database.");
            this.instances = FXCollections.observableList(new LinkedList<T>());
        } catch(Exception e) {
            Log.print("Storable", "Can't load \"" + this.name + "\".");
            Log.print("Error", e.getMessage());
            Log.print("Storable", "Creating \"" + this.name + "\" database.");
            this.instances = FXCollections.observableList(new LinkedList<T>());
        };
    };

    public void store() {
        storage.store(this.name, new LinkedList<T>(this.instances));
    };

    //#region Items
    public ObservableList<T> get() {
        return this.instances;
    };

    public void add(T t) {
        instances.add(t);
        this.store();
    };

    public void remove(T t) {
        instances.remove(t);
        this.store();
    };

    public void replace(T old, T updated) {
        int index = instances.indexOf(old);
        instances.set(index, updated);
        this.store();
    };
    //#endregion
};
