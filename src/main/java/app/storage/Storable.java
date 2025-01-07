package app.storage;

import java.io.Serializable;

import app.interfaces.StoringCallback;
import app.log.Log;
import javafx.application.Platform;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

public abstract class Storable<T extends Serializable> implements Serializable {
    private Storage storage = Storage.getInstance();
    private String name;
    protected T instances;

    private StorableLoading loading;
    private StoringCallback requestStoringCallback;
    private StoringCallback storedCallback;

    @SuppressWarnings("unchecked")
    public Storable(String name, T initial) {
        this.name = name;
        try {
            FileInputStream input = new FileInputStream("data/" + this.name + ".dat");  
            ObjectInputStream object = new ObjectInputStream(input);
            this.instances = (T) object.readObject();
            Log.print("Storable", "Loaded \"" + this.name + "\".");
            object.close();         
        } catch(FileNotFoundException e) {
            Log.print("Storable", "Creating \"" + this.name + "\".");
            this.instances = initial;
        } catch(Exception e) {
            Log.print("Storable", "Can't load \"" + this.name + "\".");
            Log.print("Error", e.getMessage());
            Log.print("Storable", "Creating \"" + this.name + "\" database.");
            this.instances = initial;
        };
    };

    //#region Store
    public void setOnRequestStoring(StoringCallback requestStoringCallback) {
        this.requestStoringCallback = requestStoringCallback;
    };

    public void setOnStored(StoringCallback storedCallback) {
        this.storedCallback = storedCallback;
    };

    public void store() {
        if(
            this.requestStoringCallback != null && 
            this.storedCallback != null &&
            this.loading == null
        ) {
            this.requestStoringCallback.call();
            this.loading = new StorableLoading();
            this.loading.start(() -> {
                Platform.runLater(() -> {
                    this.storedCallback.call();
                });

                this.loading = null;
            });
        };

        storage.store(this.name, this.instances);
    };
    //#endregion

    public T get() {
        return this.instances;
    };
};
