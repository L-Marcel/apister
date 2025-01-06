package app.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HeaderEntry implements Externalizable {
    private StringProperty key;
    private StringProperty value;  

    public HeaderEntry() {
        this.key = new SimpleStringProperty("");
        this.value = new SimpleStringProperty("");
    };

    public HeaderEntry(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    };

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.getKey());
        out.writeUTF(this.getValue());
    };

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setKey(in.readUTF());
        this.setValue(in.readUTF());
    };

    public String getKey() {
        return key.get();
    };

    public void setKey(String key) {
        this.key.set(key);
    };

    public StringProperty keyProperty() {
        return key;
    };

    public String getValue() {
        return value.get();
    };

    public void setValue(String value) {
        this.value.set(value);
    };

    public StringProperty valueProperty() {
        return value;
    };
};
