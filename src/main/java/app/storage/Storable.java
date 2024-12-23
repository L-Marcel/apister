package app.storage;

import java.io.Serializable;
import app.log.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

/**
 * Abstract class for objects that represent database tables and must be stored.
 * @param <T> the type of object to be stored
 */
public abstract class Storable<T extends Serializable> implements Serializable {
    private Storage storage = Storage.getInstance();
    private String name;
    protected T instances;

    /**
     * Constructor of the class, loads the data from the file
     * associated with the name, if it exists.
     * @param name - the name
     * @param initial - the initial instance
    */
    @SuppressWarnings("unchecked")
    public Storable(String name, T initial) {
        this.name = name;
        try {
            FileInputStream input = new FileInputStream("data/" + this.name + ".dat");  
            ObjectInputStream object = new ObjectInputStream(input);
            this.instances = (T) object.readObject();
            Log.print("Storable", "Loaded: " + this.name + ".");
            object.close();         
        } catch (FileNotFoundException e) {
            Log.print("Storable", "Creating: " + this.name + ".");
            this.instances = initial;
        } catch (Exception e) {
            Log.print("Storable", "Failed on load " + this.name + ".");
            Log.print("Error", e.getMessage());
            Log.print("Storable", "Creating " + this.name + " database.");
            this.instances = initial;
        };
    };

 
    /**
     * Requests the storage of the instance in the file.
    */
    public void store() {
        storage.store(this.name, this.instances);
    };

    /**
     * Returns the instance.
     * @return the intance
     */
    public T get() {
        return this.instances;
    };

};
