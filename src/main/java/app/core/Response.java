package app.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.Instant;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Response implements Externalizable {
    private StringProperty message;
    private StringProperty header;
    private String url;
    private StatusCode statusCode;
    private Instant requestedAt;
    private Instant receivedAt;
    
    public Response() {
        this.message = new SimpleStringProperty("");
        this.header = new SimpleStringProperty("");
    };

    public Response(
        Instant requestedAt,
        String url, 
        String message,
        String header,
        StatusCode statusCode
    ) {
        this.requestedAt = requestedAt;
        this.url = url;
        this.message = new SimpleStringProperty(message);
        this.header = new SimpleStringProperty(header);
        this.statusCode = statusCode;
        this.receivedAt = Instant.now();
    };

    //#region Externalizable
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.message.get());
        out.writeUTF(this.header.get());
        out.writeUTF(this.url);
        out.writeUTF(this.statusCode.name());
        out.writeObject(this.requestedAt);
        out.writeObject(this.receivedAt);
    };

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.message.set(in.readUTF());
        this.header.set(in.readUTF());
        this.url = in.readUTF();
        this.statusCode = StatusCode.valueOf(in.readUTF());
        this.requestedAt = (Instant) in.readObject();
        this.receivedAt = (Instant) in.readObject();
    };
    //#endregion
    //#region Getters and setters
    public StringProperty messageProperty() {
        return this.message;
    };

    public StringProperty headerProperty() {
        return this.header;
    };

    public String getUrl() {
        return this.url;
    };

    public void setUrl(String url) {
        this.url = url;
    };

    public StatusCode getStatusCode() {
        return this.statusCode;
    };

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    };

    public Instant getRequestedAt() {
        return this.requestedAt;
    };

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    };

    public Instant getReceivedAt() {
        return this.receivedAt;
    };

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    };
    //#endregion
};
