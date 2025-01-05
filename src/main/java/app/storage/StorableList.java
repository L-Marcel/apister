package app.storage;

import java.io.Serializable;
import java.util.LinkedList;

import app.log.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

/**
 * Abstract class for objects that represent database tables and must be stored
 * after each operation in a list.
 * @param <T> the type of object to be stored
 */
public abstract class StorableList<T extends Serializable> implements Serializable {
    private Storage storage = Storage.getInstance();
    private String name;
    protected ObservableList<T> instances;

    /**
     * Constructor of the class, loads the data from the file
     * associated with the name, if it exists.
     * @param name
    */
    @SuppressWarnings("unchecked")
    public StorableList(String name) {
        this.name = name;
        try {
            FileInputStream input = new FileInputStream("data/" + this.name + ".dat");  
            ObjectInputStream object = new ObjectInputStream(input);
            LinkedList<T> list = (LinkedList<T>) object.readObject();
            this.instances = FXCollections.observableList(list);
            Log.print("Storable", "Loadded " + this.instances.size() + " " + this.name + ".");
            object.close();         
        } catch(FileNotFoundException e) {
            Log.print("Storable", "Creating " + this.name + " database.");
            this.instances = FXCollections.observableList(new LinkedList<T>());
        } catch(Exception e) {
            Log.print("Storable", "Failed on load " + this.name + ".");
            Log.print("Error", e.getMessage());
            Log.print("Storable", "Creating " + this.name + " database.");
            this.instances = FXCollections.observableList(new LinkedList<T>());
        };
    };

 
    /**
     * Requests the storage of the updated instances in the file.
    */
    public void store() {
        storage.store(this.name, new LinkedList<T>(this.instances));
    };

    /**
     * Returns the list of stored instances.
     * @return the list
     */
    public ObservableList<T> get() {
        return this.instances;
    };

    /**
     * Add an instance to the list and request storage.
     * @param t - the instance
     */
    public void add(T t) {
        instances.add(t);
        this.store();
    };

    /**
     * Remove an instance from the list and request storage.
     * @param t - the instance
     */
    public void remove(T t) {
        instances.remove(t);
        this.store();
    };

    /**
     * Replace an instance in the list and request storage.
     * Useful when we want to change the type of the instantiated object.
     * @param old - the instance to be replaced
     * @param updated - the updated instance
     */
    public void replace(T old, T updated) {
        int index = instances.indexOf(old);
        instances.set(index, updated);
        this.store();
    };
};
